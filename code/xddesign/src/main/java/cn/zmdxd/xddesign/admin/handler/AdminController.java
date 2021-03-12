package cn.zmdxd.xddesign.admin.handler;

import cn.zmdxd.xddesign.admin.service.*;
import cn.zmdxd.xddesign.design.service.*;
import cn.zmdxd.xddesign.entity.*;
import cn.zmdxd.xddesign.util.FileUtil;
import cn.zmdxd.xddesign.util.ReturnResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/17 16:18
 * @description: 管理员模块
 */
@RestController
@PropertySource(value = "classpath:config.properties")
@RequestMapping(value = "/admin/", produces = {"application/json;charset=UTF-8;" })
@Transactional
public class AdminController {

    @Autowired
    private UserService userService;//用户接口
    @Autowired
    private RoleService roleService;//角色接口
    @Autowired
    private FirstLevelService firstLevelService;//产品一级分类接口
    @Autowired
    private SecondLevelService secondLevelService;//产品二级分类接口
    @Autowired
    private VideoService videoService;//视频接口
    @Autowired
    private GuideService guideService;//产品手册接口
    @Autowired
    private QuestionService questionService;//常见问题接口
    @Autowired
    private PropertyService propertyService;//产品属性接口
    @Autowired
    private ProductService productService;//产品信息接口
    @Autowired
    private ValueService valueService;//产品属性值接口
    @Autowired
    private PictureService pictureService;//产品图片接口
    @Autowired
    private CustomerService customerService;//客户接口
    @Autowired
    private SolutionsService solutionsService;//方案接口
    @Autowired
    private RoomService roomService;//房间接口
    @Autowired
    private TemplateService templateService;//模板方案接口
    @Autowired
    private ProductNumService productNumService;//产品数量接口
    @Autowired
    private QuoteService quoteService;//产品报价单接口

    @Value("${upload_path}")
    private String uploadPath;
    @Value("${file_host}")
    private String fileHost;

    //角色添加和修改
    @RequestMapping(value = "role/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdateRole(Role role) {
        //id存在，则为修改，否则新增
        if (role.getId() == null) {
            Role one = roleService.getOne(new QueryWrapper<Role>().eq("name", role.getName()), true);
            if (one != null) return ReturnResult.returnResult(false);
        }else {
            if (!roleService.getById(role.getId()).getName().equals(role.getName())) return ReturnResult.returnResult(false);
        }
        boolean saveOrUpdate = roleService.saveOrUpdate(role);
        return ReturnResult.returnResult(saveOrUpdate);
    }

    //查看角色列表
    @RequestMapping(value = "roles")
    public IPage<Role> findRoles(@RequestParam(defaultValue = "0")Integer id, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<Role> page = new Page<>(current,size);
        if (id == 0) return roleService.page(page);
        return roleService.page(page,new QueryWrapper<Role>().eq("id", id));
    }

    //查询角色列表(不分页)
    @RequestMapping(value = "roles/nopage")
    public List<Role> findRoles() {
        return roleService.list(new QueryWrapper<Role>().select("id", "name"));
    }

    //根据id查询要修改的角色
    @RequestMapping(value = "role")
    public Role findRole(Integer id) {
        return roleService.getById(id);
    }

    //删除角色
    @RequestMapping(value = "role/del", method = RequestMethod.POST)
    public ReturnResult delRole(Integer id) {
        boolean removeById;
        List<User> userList = userService.list(new QueryWrapper<User>().eq("role_id", id));
        if (userList.isEmpty()) {
            removeById = roleService.removeById(id);
            return ReturnResult.returnResult(removeById);
        }else {
            return ReturnResult.returnResult(false, "此角色不能删除");
        }
    }
    //批量删除角色
    @RequestMapping(value = "role/batchdel",method = RequestMethod.POST)
    public ReturnResult delRole(@RequestBody List<Role> roleList) {
        ReturnResult result;
        for (Role role:roleList) {
            result = delRole(role.getId());
            if (result.getStatus() == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
                return ReturnResult.returnResult(false, "其中一些角色不能删除，操作失败");
            }
        }
        return ReturnResult.returnResult(true);
    }

    //添加系统用户和修改系统用户信息
    @RequestMapping(value = "user/saveorupdate", method = RequestMethod.POST)
    public String addUser(User user) {
        return userService.saveOrUpdateUser(user);
    }

   /**
    * 查询系统用户信息
    * @param user: 要查询的用户信息，user中的id若为null，查询全部用户，否则查询单个用户
    * @param current: 当前页
    * @param size: 每页的条数
    * @return 没有id时返回分页数据IPage，有id时返回单个User
    */
    @RequestMapping(value = "user")
    public Object findUser(User user, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        return userService.findUser(user, current, size);
    }

    //查询系统用户中的设计人员列表
    @RequestMapping(value = "design")
    public List<User> findDesignList() {
        return userService.findDesignList();
    }

    //批量删除用户(假删除，把del_sign置为true)
    @RequestMapping(value = "user/batchdel", method = RequestMethod.POST)
    public ReturnResult delUser(@RequestBody List<User> userList) {
        List<Customer> customerList;
        for (User user:userList) {
            customerList = customerService.list(new QueryWrapper<Customer>().eq("design_id", user.getId()));
            if (!customerList.isEmpty()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
                return ReturnResult.returnResult(false, "其中一些用户不能删除");
            }
            boolean update = userService.update(new UpdateWrapper<User>().eq("id", user.getId()).set("del_sign", true));
            if (!update) return ReturnResult.returnResult(false);
        }
        return ReturnResult.returnResult(true);
    }
    //删除用户
    @RequestMapping(value = "user/del", method = RequestMethod.POST)
    public ReturnResult delUser(@RequestBody User user) {
        List<Customer> customerList = customerService.list(new QueryWrapper<Customer>().eq("design_id", user.getId()));
        if (!customerList.isEmpty()) return ReturnResult.returnResult(false, "不能删除此设计人员");
        boolean update = userService.update(new UpdateWrapper<User>().eq("id", user.getId()).set("del_sign", true));
        return ReturnResult.returnResult(update);
    }

    //修改客户方案信息
    @RequestMapping(value = "customer/solutions/update", method = RequestMethod.POST)
    public ReturnResult updateSolutions(Solutions solutions) {
        boolean updateById = solutionsService.updateById(solutions);
        return ReturnResult.returnResult(updateById);
    }

    //查询客户房间信息
    @RequestMapping(value = "customer/room")
    public IPage<Room> findRoom(Room room, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<Room> page = new Page<>(current, size);
        return roomService.findRoom(page, room);
    }

    //删除客户房间
    @RequestMapping(value = "customer/room/delete", method = RequestMethod.POST)
    public ReturnResult deleteRoom(Room room) {
        if (templateService.findTemplateBySoluId(room.getSoluId()) != null) {
            return ReturnResult.returnResult(false, "该房间关联了模板方案，不能删除");
        }
        boolean removeById = roomService.removeById(room.getRoomId());
        boolean removeQuoteByRoomId = quoteService.remove(new QueryWrapper<Quote>().eq("room_id", room.getRoomId()));
        if (!removeById || !removeQuoteByRoomId) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
            return ReturnResult.returnResult(false);
        }
        return ReturnResult.returnResult(true);
    }

    //批量删除客户房间
    @RequestMapping(value = "customer/room/batchdelete", method = RequestMethod.POST)
    public ReturnResult deleteRoom(@RequestBody List<Room> roomList) {
        for (Room room:roomList) {
            ReturnResult result = deleteRoom(room);
            if (result.getStatus() == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
                return ReturnResult.returnResult(false, "其中一些房间关联了模板方案，不能删除");
            }
        }
        return ReturnResult.returnResult(true);
    }

    //修改客户房间信息
    @RequestMapping(value = "customer/room/update", method = RequestMethod.POST)
    public ReturnResult updateRoom(Room room) {
        boolean update = roomService.updateById(room);
        return ReturnResult.returnResult(update);
    }

    //查询房间内产品数量信息
    @RequestMapping(value = "customer/pn")
    public IPage<ProductNum> findProductNum(ProductNum productNum, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<ProductNum> page = new Page<>(current,size);
        return productNumService.findProductNum(page, productNum);
    }

    //删除房间内产品信息
    @RequestMapping(value = "customer/pn/delete", method = RequestMethod.POST)
    public ReturnResult deleteProductNum(Integer pnId) {
        boolean removeById = productNumService.removeById(pnId);
        return ReturnResult.returnResult(removeById);
    }

    //批量删除房间内产品信息
    @RequestMapping(value = "customer/pn/batchdelete", method = RequestMethod.POST)
    public ReturnResult deleteProductNum(@RequestBody List<ProductNum> productNumList) {
        ReturnResult result;
        for (ProductNum productNum:productNumList) {
            result = deleteProductNum(productNum.getPnId());
            if (result.getStatus() == 0) {
                return ReturnResult.returnResult(false);
            }
        }
        return ReturnResult.returnResult(true);
    }

    //修改房间内产品数量
    @RequestMapping(value = "customer/pn/update", method = RequestMethod.POST)
    public ReturnResult updateProductNum(ProductNum productNum) {
        boolean updateById = productNumService.updateById(productNum);
        return ReturnResult.returnResult(updateById);
    }

    //添加或修改产品一级分类信息
    @RequestMapping(value = "firstlevel/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdateFirstLevel(FirstLevel firstLevel) {
        boolean saveOrUpdate = firstLevelService.saveOrUpdate(firstLevel);
        return ReturnResult.returnResult(saveOrUpdate);
    }

    //查询一级分类列表
    @RequestMapping(value = "firstlevel")
    public Object findFirstLevelList(Integer firstId, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "100") Integer size) {
        Page<FirstLevel> page = new Page<>(current,size);
        if (firstId == null) return firstLevelService.page(page);
        return firstLevelService.page(page, new QueryWrapper<FirstLevel>().eq("first_id",firstId)).getRecords().get(0);
    }
    //查询一级分类列表（不分页）
    @RequestMapping(value = "firstlevel/nopage")
    public List<FirstLevel> findFirstLevelList() {
        return firstLevelService.list();
    }

    //根据id删除一级分类
    @RequestMapping(value = "firstlevel/delete", method = RequestMethod.POST)
    public ReturnResult deleteFirstLevel(Integer firstId) {
        List<SecondLevel> secondLevelList = secondLevelService.list(new QueryWrapper<SecondLevel>().eq("first_id", firstId));
        if (secondLevelList.isEmpty()) {
            boolean removeById = firstLevelService.removeById(firstId);
            return ReturnResult.returnResult(removeById);
        }else {
            return ReturnResult.returnResult(false, "此行不能删除");
        }

    }
    //批量删除一级分类
    @RequestMapping(value = "firstlevel/batchdelete", method = RequestMethod.POST)
    public ReturnResult deleteFirstLevel(@RequestBody List<FirstLevel> firstLevelList) {
        for (FirstLevel firstLevel:firstLevelList) {
            ReturnResult result = deleteFirstLevel(firstLevel.getFirstId());
            if (result.getStatus() == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
                return ReturnResult.returnResult(false, "其中一行或多行不能删除");
            }
        }
        return ReturnResult.returnResult(true);
    }

    /**
     * @description: 添加或修改二级分类信息 若secondLevel中的id为null时添加信息，否则修改信息
     * @param secondLevel:二级分类信息 名称、描述
     */
    @RequestMapping(value = "secondlevel/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdate(SecondLevel secondLevel) {
        Boolean state;
        if (secondLevel.getSecondId() == null) {
            //新增二级分类信息
            state = secondLevelService.saveSecondLevel(secondLevel);

        }else {
            //修改信息，可供修改的项有 二级分类名称、二级分类描述、一级分类归属
            state = secondLevelService.update(secondLevel, new UpdateWrapper<SecondLevel>().eq("second_id", secondLevel.getSecondId()).set("first_id", secondLevel.getFirstLevel().getFirstId()));
        }
        return ReturnResult.returnResult(state);
    }

    //根据id删除产品二级分类信息
    @RequestMapping(value = "secondlevel/delete", method = RequestMethod.POST)
    public ReturnResult deleteSecondLevel(Integer secondId) {
        boolean removeById = secondLevelService.removeById(secondId);
        return ReturnResult.returnResult(removeById);
    }

    /**
     * @description: 查询二级分类列表信息
     * @param secondLevel:二级分类信息
     * @param current:当前页数
     * @param size:每页条数
     */
    @RequestMapping(value = "secondlevels")
    public IPage<SecondLevel> findSecondLevels(SecondLevel secondLevel, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<SecondLevel> page = new Page<>(current,size);
        return secondLevelService.findSecondLevels(page, secondLevel);
    }

    //根据二级分类id查询二级分类信息
    @RequestMapping(value = "secondlevel")
    public SecondLevel findSecondLevel(Integer secondId) {
        return secondLevelService.findSecondLevel(secondId);
    }

    //查询二级分类列表（不分页）
    @RequestMapping(value = "secondlevel/nopage")
    public List<SecondLevel> findSecondLevelList(Integer firstId) {
        return secondLevelService.list(new QueryWrapper<SecondLevel>().eq("first_id", firstId).select("second_id","second_name"));
    }

    //查询二级分类产品属性列表
    @RequestMapping(value = "secondlevel/property")
    public IPage<Property> findPropertyList(Property property, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<Property> page = new Page<>(current, size);
        return propertyService.findPropertyList(page, property);
    }

    //查询二级分类产品属性列表（不分页）
    @RequestMapping(value = "secondlevel/property/nopage")
    public List<Property> findPropertyList(Integer secondId) {
        return propertyService.list(new QueryWrapper<Property>().eq("second_id", secondId).select("property_id", "property_name", "common_value"));
    }

    /**
     * @description: 在二级分类下添加或修改产品属性信息
     * @param property:属性信息(名称、描述、常用值)
     */
    @RequestMapping(value = "property/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdateProperty(Property property) {
        boolean state;
        if (property.getPropertyId() == null) {
            //添加属性
            if (property.getSecondLevel().getSecondId() == null) {
                return ReturnResult.returnResult(false, "必须选择一个二级分类");
            }
            state = propertyService.saveProperty(property);
        }else {
            //修改属性
            state = propertyService.updateById(property);
        }
        return ReturnResult.returnResult(state);

    }

    //根据属性id查询属性信息
    @RequestMapping(value = "property")
    public Property findProperty(Integer propertyId) {
        Property property = propertyService.getById(propertyId);
        String[] strings = property.getCommonValue().split("，");
        List<String> stringList = Arrays.asList(strings);
        property.setCommonValueList(stringList);
        return property;
    }

    //根据id删除属性信息
    @RequestMapping(value = "property/delete")
    public ReturnResult deleteProperty(Integer propertyId) {
        List<PropertyValue> valueList = valueService.list(new QueryWrapper<PropertyValue>().eq("property_id", propertyId));
        if (valueList.isEmpty()) {
            boolean removeById = propertyService.removeById(propertyId);
            return ReturnResult.returnResult(removeById);
        }else {
            return ReturnResult.returnResult(false, "此行关联产品属性，不能删除");
        }
    }

    //批量删除属性信息
    @RequestMapping(value = "property/batchdelete")
    public ReturnResult deleteProperty(@RequestBody List<Property> propertyList) {
        ReturnResult result;
        for (Property property:propertyList) {
            result = deleteProperty(property.getPropertyId());
            if (result.getStatus() == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
                return ReturnResult.returnResult(false, "其中一些行关联了产品属性，不能删除");
            }
        }
        return ReturnResult.returnResult(true);
    }

    //根据二级分类id查询产品列表（不分页）
    @RequestMapping(value = "secondlevel/products/nopage")
    public List<Product> findProductsBySecondLevel(Integer secondId) {
        return productService.list(new QueryWrapper<Product>().eq("second_id", secondId).select("product_id", "product_name"));
    }

    //查询产品属性信息
    @RequestMapping(value = "product/property")
    public IPage<ProductVo> findProductPropertyValueList(ProductVo productVo, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<ProductVo> page = new Page<>(current, size);
        String productName = productVo.getProductName();
        if (productName != null) {
            productName = productName.trim();
        }
        productVo.setProductName(productName);
        return valueService.findProductPropertyValueList(page, productVo);
    }

    //添加产品属性信息（单个添加）
    @RequestMapping(value = "product/property/add", method = RequestMethod.POST)
    public ReturnResult saveProductProperty(ProductVo productVo) {
        if (productVo.getProductId() == null || productVo.getPropertyId() == null) {
            return ReturnResult.returnResult(false, "必填项不能为空！");
        }
        List<ProductProperty> productPropertyList = valueService.findByProductIdAndPropertyId(productVo);
        if (productPropertyList.isEmpty()) {
            PropertyValue propertyValue = new PropertyValue();
            Property property = new Property();
            property.setPropertyId(productVo.getPropertyId());
            propertyValue.setValueName(productVo.getValueName());
            propertyValue.setProperty(property);
            Integer save = valueService.saveValue(propertyValue);
            if (save != 1) {
                return ReturnResult.returnResult(false);
            }
            boolean state = productService.saveProductProperty(productVo.getProductId(), propertyValue.getValueId());//将产品id和属性值id保存到t_product_property中
            if (!state) {
                return ReturnResult.returnResult(false);
            }
            return ReturnResult.returnResult(true);

        }else {
            return ReturnResult.returnResult(false, "该产品已有此属性");
        }
    }


    //修改产品某个属性信息
    @RequestMapping(value = "product/property/update", method = RequestMethod.POST)
    public ReturnResult updateProductProperty(PropertyValue propertyValue) {
        boolean state = valueService.update(new UpdateWrapper<PropertyValue>().eq("value_id",propertyValue.getValueId()).set("value_name",propertyValue.getValueName()));
        return ReturnResult.returnResult(state);
    }

    //删除产品某个属性
    @RequestMapping(value = "/product/property/delete", method = RequestMethod.POST)
    public ReturnResult deleteProductProperty(Integer valueId) {
        ReturnResult result = new ReturnResult();
        boolean state2 = productService.removeProductProperty(valueId);
        boolean state1 = valueService.removeById(valueId);
        if (state2 && state1) {
            result.setStatus(1);
            result.setMsg("删除成功");
        }else {
            result.setStatus(0);
            result.setMsg("删除失败，请稍后重试");
        }
        return result;
    }

    /**
     * @description: 添加或修改产品视频信息
     * @param video:视频信息（视频名称、视频描述、视频链接）
     * @param file:上传的视频
     */
    @RequestMapping(value = "video/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdateVideo(Video video, @RequestParam(value = "file", required = false) MultipartFile file) {
        ReturnResult result = new ReturnResult();
        int status;
        String msg;
        if (video.getVideoId() == null) {
            if (file.getSize() != 0) {
                //添加视频信息、上传视频
                String saveFileName = FileUtil.saveFile(file, uploadPath + "/video/" + video.getVideoName());//上传路径+文件名
                String videoLink = fileHost + saveFileName;//保存到数据库中的文件链接
                video.setVideoLink(videoLink);
                boolean saveOrUpdate = videoService.saveOrUpdate(video);
                if (!saveOrUpdate) {
                    status = 0;
                    msg = "添加失败，请稍后重试";
                }else {
                    status = 1;
                    msg = "添加成功";
                }
            }else {
                status = 0;
                msg = "请上传文件";
            }
        }else {
            //修改视频信息
            if (file.getSize() != 0) {
                String saveFileName = FileUtil.saveFile(file, uploadPath + "/video/" + video.getVideoName());
                String videoLink = fileHost + saveFileName;
                video.setVideoLink(videoLink);
            }
            boolean update = videoService.update(new UpdateWrapper<Video>().eq("video_id", video.getVideoId()).set("video_name", video.getVideoName()).set("video_link", video.getVideoLink()).set("video_desc", video.getVideoDesc()).set("video_reserve", video.getVideoReserve()));
            if (!update) {
                status = 0;
                msg = "修改失败，请稍后重试";
            }else {
                status = 1;
                msg = "修改成功";
            }
        }
        result.setMsg(msg);
        result.setStatus(status);
        return result;
    }

    //产品视频信息的删除
    @RequestMapping(value = "video/delete",method = RequestMethod.POST)
    public ReturnResult deleteVideo(@RequestBody ArrayList<Integer> ids) {
        ReturnResult result = new ReturnResult();
        Video video;
        for (Integer id:ids){
            video = videoService.getById(id);
            boolean deleteFile = FileUtil.deleteFile(uploadPath + video.getVideoLink().substring(fileHost.length()));//删除视频文件
            if (!deleteFile) {
                result.setStatus(0);
                result.setMsg("删除失败，请稍后重试");
                return result;
            }
        }
        boolean removeByIds = videoService.removeByIds(ids);//删除数据库中的视频信息
        return ReturnResult.returnResult(removeByIds);
    }

    /**
     * @description: 添加或修改产品手册信息
     * @param picture:手册封面图片
     * @param guide:产品手册信息
     * @param file:上传的文件
     */
    @RequestMapping(value = "guide/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdateGuide(Guide guide,@RequestParam(value = "picture", required = false) MultipartFile picture, @RequestParam(value = "file", required = false) MultipartFile file) {
        if (file.getSize() != 0) {
            String saveFileName = FileUtil.saveFile(file, uploadPath + "/guide/" + guide.getGuideName());//上传路径名
            String guideLink = fileHost+saveFileName;//保存到数据库中的链接
            guide.setGuideLink(guideLink);
        }
        if (picture.getSize() != 0) {
            String savePictureName = FileUtil.saveFile(picture, uploadPath+"/guide/"+guide.getGuideName());
            String pictureLink = fileHost + savePictureName;
            guide.setPictureLink(pictureLink);
        }
        boolean saveOrUpdate = guideService.saveOrUpdate(guide);
        return ReturnResult.returnResult(saveOrUpdate);
    }

    //批量删除产品手册信息
    @RequestMapping(value = "guide/delete", method = RequestMethod.POST)
    public ReturnResult deleteGuide(@RequestBody ArrayList<Integer> ids) {
        ReturnResult result = new ReturnResult();
        Guide guide;
        for (Integer id:ids) {
            guide = guideService.getById(id);
            boolean deleteFile = FileUtil.deleteFile(uploadPath + guide.getGuideLink().substring(fileHost.length()));
            if (!deleteFile) {
                result.setStatus(0);
                result.setMsg("删除失败，请稍后重试");
                return result;
            }
        }

        boolean removeByIds = guideService.removeByIds(ids);
        return ReturnResult.returnResult(removeByIds);
    }

    //添加或修改常见问题信息
    @RequestMapping(value = "question/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdate(Question question) {
        boolean saveOrUpdate = questionService.saveOrUpdate(question);
        return ReturnResult.returnResult(saveOrUpdate);
    }

    //批量删除常见问题
    @RequestMapping(value = "question/delete", method = RequestMethod.POST)
    public ReturnResult deleteQuestion(@RequestBody ArrayList<Integer> ids) {
        boolean removeByIds = questionService.removeByIds(ids);
        return ReturnResult.returnResult(removeByIds);
    }

    @RequestMapping(value = "test", method = RequestMethod.POST)
    public ReturnResult uploadPic(ProductVo productVo, @RequestParam(value = "files", required = false) MultipartFile[] files, String name) {
        System.out.println(productVo);
        System.out.println(name);
//        System.out.println(files.length);
        return ReturnResult.returnResult(true, "上传成功");
    }

    /**
     * @description: 添加产品信息
     * @param productVo:产品信息
     * @param files:产品图片列表
     */
    @RequestMapping(value = "product/save", method = RequestMethod.POST)
    public ReturnResult saveProduct(ProductVo productVo, @RequestParam(value = "files", required = false) MultipartFile[] files) {
        ReturnResult result = new ReturnResult();
        Integer productId = productService.saveProduct(productVo);//将产品基本信息保存到t_product表中
        if (productId != 1) {
            result.setStatus(0);
            result.setMsg("添加失败，请稍后重试");
        }else {
            List<Map<String, Object>> propertyValueList = productVo.getPropertyValueList();//获取属性id-属性值列表
            PropertyValue propertyValue = new PropertyValue();
            Property property = new Property();
            for (Map<String, Object> pv:propertyValueList) {
                propertyValue.setValueName(pv.get("valueName").toString());//获取属性值
                property.setPropertyId(Integer.valueOf(pv.get("propertyId").toString()));//获取属性id
                propertyValue.setProperty(property);
                Integer valueId = valueService.saveValue(propertyValue);//将属性值和属性id保存到表t_property_value中
                if (valueId != 1) {
                    result.setStatus(0);
                    result.setMsg("添加失败，请稍后重试");
                    return result;
                }else {
                    boolean state = productService.saveProductProperty(productVo.getProductId(),propertyValue.getValueId());//将产品id和属性值id保存到t_product_property中
                    if(!state) {
                        result.setStatus(0);
                        result.setMsg("添加失败，请稍后重试");
                        return result;
                    }
                }
            }

            if (files != null) {
                Picture picture = new Picture();
                Picture picture1 = pictureService.getOne(new QueryWrapper<Picture>().eq("product_id", productVo.getProductId()).eq("default_picture", true), true);//查看该产品图片是否已经有默认图片
                int i = 0;
                for (MultipartFile file:files) {
                    if (file.getSize() == 0) {
                        result.setStatus(0);
                        result.setMsg("没有上传任何图片");
                        return result;
                    }

                    String saveFileName = FileUtil.saveFile(file, uploadPath + "/picture/" + productVo.getProductName());//上传图片到nginx服务器
                    String pictureLink = fileHost+saveFileName;
                    String pictureDesc = "这是一张"+productVo.getProductName()+"图片";
                    picture.setPictureName(productVo.getProductName());
                    picture.setPictureLink(pictureLink);
                    picture.setPictureDesc(pictureDesc);
                    if (picture1 == null) picture.setDefaultPicture(i == 0);//默认将第一张图片设置为默认图片
                    else picture.setDefaultPicture(false);//已经有默认图片则不设置
                    boolean state = pictureService.savePicture(picture, productVo.getProductId());//将图片信息保存到t_picture表中
                    if(!state) {
                        result.setStatus(0);
                        result.setMsg("添加失败，请稍后重试");
                        return result;
                    }

                    i = i + 1;
                }
            }

            result.setStatus(1);
            result.setMsg("添加成功");
        }
        return result;
    }

    /**
     * @description: 查看产品列表
     * @param current:当前页
     * @param size:每页条数
     */
    @RequestMapping(value = "products", method = RequestMethod.GET)
    public Object findProducts(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size, ProductVo productVo) {
        //由于mybatis-plus对一表对多表left join查询的支持欠缺，所以不用它的分页查询
        Page<Product> page = new Page<>(1,100000000);
        page.setSearchCount(false);
        String productName = productVo.getProductName();
        if (productName != null) {
            productVo.setProductName(productName.trim());
        }
        return productService.findProducts(page, current, size, productVo);
    }

    //修改产品基本信息
    @RequestMapping(value = "product/update", method = RequestMethod.POST)
    public ReturnResult updateProduct(Product product) {
        boolean update = productService.update(new UpdateWrapper<Product>().eq("product_id", product.getProductId()).set("product_name", product.getProductName()).set("product_link", product.getProductLink()).set("product_models", product.getProductModels()).set("product_desc", product.getProductDesc()).set("price", product.getPrice()).set("product_reserve1", product.getProductReserve1()).set("product_reserve2", product.getProductReserve2()).set("product_reserve3", product.getProductReserve3()).set("product_reserve4", product.getProductReserve4()).set("product_reserve5", product.getProductReserve5()));
        return ReturnResult.returnResult(update);
    }

    /**
     * @description: 单独添加产品图片(一张一张添加)
     * @param productId:产品id
     * @param productName:产品名称
     * @param files:上传的图片
     */
    @RequestMapping(value = "product/picture/save", method = RequestMethod.POST)
    public ReturnResult saveProductPicture(Integer productId, String productName, @RequestParam(value = "files", required = false) MultipartFile[] files) {
        ReturnResult result = new ReturnResult();
        int i = 0;
        String saveFileName,pictureLink,pictureDesc;
        boolean state;
        Picture picture = new Picture();
        Picture picture1 = pictureService.getOne(new QueryWrapper<Picture>().eq("product_id", productId).eq("default_picture", true), true);//查看该产品图片是否已经有默认图片
        for (MultipartFile file:files) {
            if (file.getSize() == 0) {
                result.setStatus(0);
                result.setMsg("没有上传任何图片");
                return result;
            }
            saveFileName = FileUtil.saveFile(file, uploadPath + "/picture/" + productName);
            pictureLink = fileHost+saveFileName;
            pictureDesc = "这是一张" + productName + "图片";
            picture.setPictureName(productName);
            picture.setPictureLink(pictureLink);
            picture.setPictureDesc(pictureDesc);
            if (picture1 == null) picture.setDefaultPicture(i == 0);
            else picture.setDefaultPicture(false);
            state = pictureService.savePicture(picture, productId);
            if(!state) {
                result.setStatus(0);
                result.setMsg("添加失败，请稍后重试");
                return result;
            }
            i = i + 1;
        }
        result.setStatus(1);
        result.setMsg("添加成功");
        return result;
    }

    /**
     * @description: 批量删除某个产品下的图片
     * @param ids:idList集合
     */
    @RequestMapping(value = "product/picture/delete", method = RequestMethod.POST)
    public ReturnResult deleteProductPicture(@RequestBody List<Integer> ids) {
        ReturnResult result = new ReturnResult();
        Picture picture;
        String pictureLink;
        boolean deleteFile;
        for (Integer id:ids) {
            picture = pictureService.getPictureById(id);//???->null
            pictureLink = picture.getPictureLink();
            deleteFile = FileUtil.deleteFile(uploadPath + pictureLink.substring(fileHost.length()));
            if (!deleteFile) {
                result.setStatus(0);
                result.setMsg("删除失败，请稍后重试");
                return result;
            }
        }
        boolean removeByIds = pictureService.removeByIds(ids);
        if (!removeByIds) {
            result.setStatus(0);
            result.setMsg("删除失败，请稍后重试");

        }else {
            result.setStatus(1);
            result.setMsg("删除成功");
        }
        return result;
    }


}
