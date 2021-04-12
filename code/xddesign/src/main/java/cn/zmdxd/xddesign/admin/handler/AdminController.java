package cn.zmdxd.xddesign.admin.handler;

import cn.zmdxd.xddesign.admin.service.*;
import cn.zmdxd.xddesign.design.service.*;
import cn.zmdxd.xddesign.entity.*;
import cn.zmdxd.xddesign.util.FileType;
import cn.zmdxd.xddesign.util.FileUtil;
import cn.zmdxd.xddesign.util.ReturnResult;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private RenderingsService renderingsService;//方案效果图接口

    @Value("${upload_path}")
    private String uploadPath;
    @Value("${file_host}")
    private String fileHost;
    @Value("${upload_path_2}")
    private String uploadPath2;

    // 角色添加和修改
    @RequestMapping(value = "role/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdateRole(Role role) {
        //id存在，则为修改，否则新增
        if (role.getId() == null) {
            Role one = roleService.getOne(new QueryWrapper<Role>().eq("name", role.getName()), true);
            if (one != null) {
                return ReturnResult.returnResult(false);
            }
        }else {
            if (!roleService.getById(role.getId()).getName().equals(role.getName())) {
                return ReturnResult.returnResult(false);
            }
        }
        boolean saveOrUpdate = roleService.saveOrUpdate(role);
        return ReturnResult.returnResult(saveOrUpdate);
    }

    // 查看角色列表
    @RequestMapping(value = "roles")
    public IPage<Role> findRoles(@RequestParam(defaultValue = "0")Integer id, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<Role> page = new Page<>(current,size);
        if (id == 0) {
            return roleService.page(page);
        }
        return roleService.page(page,new QueryWrapper<Role>().eq("id", id));
    }

    // 查询角色列表(不分页)
    @RequestMapping(value = "roles/nopage")
    public List<Role> findRoles() {
        return roleService.list(new QueryWrapper<Role>().select("id", "name"));
    }

    // 根据id查询要修改的角色
    @RequestMapping(value = "role")
    public Role findRole(Integer id) {
        return roleService.getById(id);
    }

    // 删除角色
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
    // 批量删除角色
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

    // 添加系统用户和修改系统用户信息
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

    // 查询系统用户中的设计人员列表
    @RequestMapping(value = "design")
    public List<User> findDesignList() {
        return userService.findDesignList();
    }

    // 批量删除用户(假删除，把del_sign置为true)
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
            if (!update) {
                return ReturnResult.returnResult(false);
            }
        }
        return ReturnResult.returnResult(true);
    }
    // 删除用户
    @RequestMapping(value = "user/del", method = RequestMethod.POST)
    public ReturnResult delUser(@RequestBody User user) {
        List<Customer> customerList = customerService.list(new QueryWrapper<Customer>().eq("design_id", user.getId()));
        if (!customerList.isEmpty()) {
            return ReturnResult.returnResult(false, "不能删除此设计人员");
        }
        boolean update = userService.update(new UpdateWrapper<User>().eq("id", user.getId()).set("del_sign", true));
        return ReturnResult.returnResult(update);
    }

    // 管理员修改客户方案信息
    @RequestMapping(value = "customer/solutions/update", method = RequestMethod.POST)
    public ReturnResult updateSolutions(Solutions solutions) {
        boolean updateById = solutionsService.updateById(solutions);
        return ReturnResult.returnResult(updateById);
    }

    // 管理员查询客户方案效果图
    @RequestMapping(value = "solutions/renderings")
    public IPage<Renderings> findRenderings(Room room, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<Renderings> page = new Page<>(current, size);
        return renderingsService.findRenderings(page, room);
    }

    // 管理员查询客户方案报价表
    @RequestMapping(value = "solutions/quotes")
    public IPage<Quote> findQuotes(Room room, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<Quote> page = new Page<>(current, size);
        return quoteService.findQuotes(page ,room);
    }

    // 查询客户房间信息
    @RequestMapping(value = "customer/room")
    public IPage<Room> findRoom(Room room, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<Room> page = new Page<>(current, size);
        return roomService.findRoom(page, room);
    }

    // 删除客户房间
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

    // 批量删除客户房间
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

    // 修改客户房间信息
    @RequestMapping(value = "customer/room/update", method = RequestMethod.POST)
    public ReturnResult updateRoom(Room room) {
        boolean update = roomService.updateById(room);
        return ReturnResult.returnResult(update);
    }

    // 查询房间内产品数量信息
    @RequestMapping(value = "customer/pn")
    public IPage<ProductNum> findProductNum(ProductNum productNum, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<ProductNum> page = new Page<>(current,size);
        return productNumService.findProductNum(page, productNum);
    }

    // 删除房间内产品信息
    @RequestMapping(value = "customer/pn/delete", method = RequestMethod.POST)
    public ReturnResult deleteProductNum(Integer pnId) {
        boolean removeById = productNumService.removeById(pnId);
        return ReturnResult.returnResult(removeById);
    }

    // 批量删除房间内产品信息
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

    // 修改房间内产品数量
    @RequestMapping(value = "customer/pn/update", method = RequestMethod.POST)
    public ReturnResult updateProductNum(ProductNum productNum) {
        boolean updateById = productNumService.updateById(productNum);
        return ReturnResult.returnResult(updateById);
    }

    // 添加或修改产品一级分类信息
    @RequestMapping(value = "firstlevel/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdateFirstLevel(FirstLevel firstLevel) {
        boolean saveOrUpdate = firstLevelService.saveOrUpdate(firstLevel);
        return ReturnResult.returnResult(saveOrUpdate);
    }

    // 查询一级分类列表
    @RequestMapping(value = "firstlevel")
    public Object findFirstLevelList(Integer firstId, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "100") Integer size) {
        Page<FirstLevel> page = new Page<>(current,size);
        if (firstId == null) {
            return firstLevelService.page(page);
        }
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
        } else {
            //修改信息，可供修改的项有 二级分类名称、二级分类描述、一级分类归属
            state = secondLevelService.update(secondLevel, new UpdateWrapper<SecondLevel>().eq("second_id", secondLevel.getSecondId()).set("first_id", secondLevel.getFirstLevel().getFirstId()));
        }

        return ReturnResult.returnResult(state);
    }

    // 根据id删除产品二级分类信息
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

    // 根据二级分类id查询二级分类信息
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
            // 添加属性
            if (property.getSecondLevel().getSecondId() == null) {
                return ReturnResult.returnResult(false, "必须选择一个二级分类");
            }
            state = propertyService.saveProperty(property);
        }else {
            // 修改属性
            state = propertyService.updateById(property);
        }
        return ReturnResult.returnResult(state);

    }

    // 根据属性id查询属性信息
    @RequestMapping(value = "property")
    public Property findProperty(Integer propertyId) {
        Property property = propertyService.getById(propertyId);
        String[] strings = property.getCommonValue().split("，");
        List<String> stringList = Arrays.asList(strings);
        property.setCommonValueList(stringList);
        return property;
    }

    // 根据id删除属性信息
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

    // 批量删除属性信息
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

    // 根据二级分类id查询产品列表（不分页）
    @RequestMapping(value = "secondlevel/products/nopage")
    public List<Product> findProductsBySecondLevel(Integer secondId) {
        return productService.list(new QueryWrapper<Product>().eq("second_id", secondId).select("product_id", "product_name"));
    }

    // 查询产品属性信息
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

    // 添加产品属性信息（单个添加）
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
        } else {
            return ReturnResult.returnResult(false, "该产品已有此属性");
        }
    }


    // 修改产品某个属性信息
    @RequestMapping(value = "product/property/update", method = RequestMethod.POST)
    public ReturnResult updateProductProperty(PropertyValue propertyValue) {
        boolean state = valueService.update(new UpdateWrapper<PropertyValue>().eq("value_id",propertyValue.getValueId()).set("value_name",propertyValue.getValueName()));
        return ReturnResult.returnResult(state);
    }

    // 删除产品某个属性
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

    // 产品视频上传
    @RequestMapping(value = "video/upload", method = RequestMethod.POST)
    public JSONObject uploadProductVideo(@RequestParam(value = "file", required = false) MultipartFile file) {
        JSONObject res = new JSONObject();
        JSONObject data = new JSONObject();
        if (file.getSize() != 0) {
            String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String saveFileName = FileUtil.saveFile(file, uploadPath + uploadPath2 + "/video/" + date, uploadPath);
            String link = fileHost + saveFileName;
            res.put("status", 1);
            res.put("msg", "上传成功");
            data.put("src", link);
            res.put("data", data);
        }else {
            res.put("status", 0);
            res.put("msg", "上传失败");
        }
        return res;
    }

    /**
     * @description: 添加或修改产品视频信息
     * @param video:视频信息（视频名称、视频描述、视频链接）
     */
    @RequestMapping(value = "video/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdateVideo(Video video) {
        if (video.getVideoId() == null && video.getProductId() == null) {
            return ReturnResult.returnResult(false, "请先选择一个产品");
        }
        if (video.getVideoId() == null) {
            video.setVideoName(video.getVideoName() + "安装使用视频");
            video.setVideoDesc("这是" + video.getVideoName());
        }
        boolean saveOrUpdate = videoService.saveOrUpdate(video);
        return ReturnResult.returnResult(saveOrUpdate);
    }

    // 产品视频信息的删除
    @RequestMapping(value = "video/batchdelete",method = RequestMethod.POST)
    public ReturnResult deleteVideo(@RequestBody ArrayList<Integer> ids) {
        Video video;
        for (Integer id:ids){
            video = videoService.getById(id);
            FileUtil.deleteFile(uploadPath + video.getVideoLink().substring(fileHost.length()));//删除视频文件
        }
        boolean removeByIds = videoService.removeByIds(ids);//删除数据库中的视频信息
        return ReturnResult.returnResult(removeByIds);
    }

    // 上传产品手册和产品手册封面
    @RequestMapping(value = "guide/upload", method = RequestMethod.POST)
    public JSONObject uploadGuide(@RequestParam(value = "file") MultipartFile file) {
        JSONObject res = new JSONObject();
        JSONObject data = new JSONObject();
        if (file.getSize() != 0) {
            String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String saveFileName = FileUtil.saveFile(file, uploadPath + uploadPath2 + "/guide/" + date, uploadPath);
            String link = fileHost + saveFileName;
            res.put("status", 1);
            res.put("msg", "上传成功");
            data.put("src", link);
            res.put("data", data);
        }else {
            res.put("status", 0);
            res.put("msg", "上传失败");
        }
        return res;
    }

    /**
     * @description: 添加或修改产品手册信息
     * @param guide:产品手册
     */
    @RequestMapping(value = "guide/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdateGuide(Guide guide) {
        if (guide.getGuideId() == null && guide.getProductId() == null) {
            return ReturnResult.returnResult(false, "请先选择一个产品");
        }
        if (guide.getGuideId() == null) {
            guide.setGuideName(guide.getGuideName() + "产品手册");
            guide.setGuideDesc("这是" + guide.getGuideName());
        }else {
            List<Guide> guides = guideService.list(new QueryWrapper<Guide>().eq("guide_id", guide.getGuideId()));
            String guideName = guides.get(0).getGuideName();
            guide.setGuideName(guideName);
            guide.setGuideDesc("这是" + guideName);
        }
        boolean saveOrUpdate = guideService.saveOrUpdate(guide);
        return ReturnResult.returnResult(saveOrUpdate);
    }

    // 批量删除产品手册信息
    @RequestMapping(value = "guide/batchdelete", method = RequestMethod.POST)
    public ReturnResult deleteGuide(@RequestBody ArrayList<Integer> ids) {
        Guide guide;
        for (Integer id:ids) {
            guide = guideService.getById(id);
            FileUtil.deleteFile(uploadPath + guide.getGuideLink().substring(fileHost.length()));
            FileUtil.deleteFile(uploadPath + guide.getPictureLink().substring(fileHost.length()));
        }

        boolean removeByIds = guideService.removeByIds(ids);
        return ReturnResult.returnResult(removeByIds);
    }

    // 添加或修改常见问题
    @RequestMapping(value = "question/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdate(Question question) {
        if (question.getProductId() == null && question.getId() == null) {
            return ReturnResult.returnResult(false, "必须选择一个产品");
        }
        boolean saveOrUpdate = questionService.saveOrUpdate(question);
        return ReturnResult.returnResult(saveOrUpdate);
    }

    //删除常见问题
    @RequestMapping(value = "question/delete", method = RequestMethod.POST)
    public ReturnResult deleteQuestion(Integer id) {
        boolean removeById = questionService.removeById(id);
        return ReturnResult.returnResult(removeById);
    }

    // 批量删除常见问题
    @RequestMapping(value = "question/batchdelete", method = RequestMethod.POST)
    public ReturnResult deleteQuestion(@RequestBody ArrayList<Integer> ids) {
        boolean removeByIds = questionService.removeByIds(ids);
        return ReturnResult.returnResult(removeByIds);
    }

    // 查询常见问题列表
    @RequestMapping(value = "questions")
    public IPage<Question> findQuestions(ProductVo productVo, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<Question> page = new Page<>(current, size);
        return questionService.findQuestions(page, productVo);
    }

    // 查询产品视频列表
    @RequestMapping(value = "videos")
    public IPage<Video> findVideos(ProductVo productVo, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<Video> page = new Page<>(current,size);
        return videoService.findVideos(page, productVo);
    }

    // 查询产品手册列表
    @RequestMapping(value = "guides")
    public IPage<Guide> findGuideList(ProductVo productVo, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<Guide> page = new Page<>(current, size);
        return guideService.findGuides(page, productVo);
    }

    /**
     * @description: 添加产品
     * @param productVo:产品信息
     * @param files:产品图片列表
     */
    @RequestMapping(value = "product/save", method = RequestMethod.POST)
    public ReturnResult saveProduct(ProductVo productVo, @RequestParam(value = "files", required = false) MultipartFile[] files) throws IOException {
        if (files.length == 0) {
            return ReturnResult.returnResult(false, "必须上传至少一张图片");
        }
        ReturnResult result = new ReturnResult();
        Integer productId = productService.saveProduct(productVo);//将产品基本信息保存到t_product表中
        if (productId != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
            result.setStatus(0);
            result.setMsg("添加失败，请稍后重试");
        }else {
            List<Map<String, Object>> propertyValueList = productVo.getPropertyValueList();//获取属性id-属性值列表
            if (propertyValueList == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
                return ReturnResult.returnResult(false, "需要为产品添加属性信息");
            }
            PropertyValue propertyValue = new PropertyValue();
            Property property = new Property();
            for (Map<String, Object> pv:propertyValueList) {
                propertyValue.setValueName(pv.get("valueName").toString());//获取属性值
                property.setPropertyId(Integer.valueOf(pv.get("propertyId").toString()));//获取属性id
                propertyValue.setProperty(property);
                Integer valueId = valueService.saveValue(propertyValue);//将属性值和属性id保存到表t_property_value中
                if (valueId != 1) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
                    result.setStatus(0);
                    result.setMsg("添加失败，请稍后重试");
                    return result;
                }else {
                    boolean state = productService.saveProductProperty(productVo.getProductId(),propertyValue.getValueId());//将产品id和属性值id保存到t_product_property中
                    if(!state) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
                        result.setStatus(0);
                        result.setMsg("添加失败，请稍后重试");
                        return result;
                    }
                }
            }

            Picture picture = new Picture();
            Picture picture1 = pictureService.getOne(new QueryWrapper<Picture>().eq("product_id", productVo.getProductId()).eq("default_picture", true), true);//查看该产品图片是否已经有默认图片
            int i = 0;

            CommonsMultipartFile commonsMultipartFile;
            DiskFileItem fileItem;
            InputStream ips;
            FileType fileType;

            for (MultipartFile file:files) {

                // 判断上传的文件是否为图片
                commonsMultipartFile = (CommonsMultipartFile) file;
                fileItem = (DiskFileItem) commonsMultipartFile.getFileItem();
                ips = fileItem.getInputStream();
                fileType = FileUtil.getFileType(ips);
                if (fileType == null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
                    return ReturnResult.returnResult(false, "图片格式错误");
                }

                String saveFileName = FileUtil.saveFile(file, uploadPath + uploadPath2 + "/picture/" + productVo.getProductName(), uploadPath);//上传图片到nginx服务器
                String pictureLink = fileHost + saveFileName;
                String pictureDesc = "这是一张"+productVo.getProductName()+"图片";
                picture.setPictureName(productVo.getProductName());
                picture.setPictureLink(pictureLink);
                picture.setPictureDesc(pictureDesc);
                if (picture1 == null) {
                    // 默认将第一张图片设置为默认图片
                    picture.setDefaultPicture(i == 0);
                } else {
                    picture.setDefaultPicture(false);//已经有默认图片则不设置
                }
                boolean state = pictureService.savePicture(picture, productVo.getProductId());//将图片信息保存到t_picture表中
                if(!state) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
                    result.setStatus(0);
                    result.setMsg("添加失败，请稍后重试");
                    return result;
                }

                i = i + 1;
            }

            result.setStatus(1);
            result.setMsg("添加成功");
        }
        return result;
    }

    //修改产品基本信息
    @RequestMapping(value = "product/update", method = RequestMethod.POST)
    public ReturnResult updateProduct(Product product, Integer secondId) {
        if (secondId == null || secondId == 0) {
            return ReturnResult.returnResult(false, "需要为产品指定分类");
        }
        boolean update = productService.update(product, new UpdateWrapper<Product>().eq("product_id", product.getProductId()).set("second_id", secondId));
        return ReturnResult.returnResult(update);
    }

    // 删除产品
    @RequestMapping(value = "product/del", method = RequestMethod.POST)
    public ReturnResult deleteProduct(Integer productId) {
        List<ProductNum> productNumList = productNumService.list(new QueryWrapper<ProductNum>().eq("product_id", productId).select("pn_id"));
        System.out.println(productNumList);
        if (!productNumList.isEmpty()) {
            return ReturnResult.returnResult(false, "客户方案用到了该产品，不能删除");
        }

        List<ProductProperty> productPropertyList = productService.findProductPropertyByProductId(productId);//查看t_product_property表中有无要删除的信息
        if (!productPropertyList.isEmpty()) {
            boolean removeProductPropertyByProductId = productService.removeProductPropertyByProductId(productId);// 删除t_product_property表的信息
            if (!removeProductPropertyByProductId) {
                return ReturnResult.returnResult(false);
            }
            for (ProductProperty pp:productPropertyList) {
                boolean removeByValueId = valueService.removeById(pp.getValue().getValueId());//删除t_property_value表中的信息
                if (!removeByValueId) {
                    return ReturnResult.returnResult(false);
                }
            }
        }

        // 还需考虑库存表、进库表、出库表，暂时没写，以后再写

        // 删除t_product表中的信息
        boolean removeById = productService.removeById(productId);
        return ReturnResult.returnResult(removeById);
    }

    //查询产品图片列表
    @RequestMapping(value = "product/picture")
    public IPage<Picture> findPictures(ProductVo productVo, @RequestParam(value = "current", defaultValue = "1") Integer current, @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (productVo != null) {
            if (productVo.getProductName() != null) {
                productVo.setProductName(productVo.getProductName().trim());
            }
        }
        Page<Picture> page = new Page<>(current, size);
        return pictureService.findPicture(page, productVo);
    }

    /**
     * @description: 单独添加产品图片
     * @param productId:产品id
     * @param productName:产品名称
     * @param file:上传的图片
     */
    @RequestMapping(value = "product/picture/save", method = RequestMethod.POST)
    public ReturnResult saveProductPicture(Integer productId, String productName, @RequestParam(value = "file", required = false) MultipartFile file) {
        if (productId == null) {
            return ReturnResult.returnResult(false, "需要选择一个产品");
        }
        String saveFileName,pictureLink,pictureDesc;
        boolean state;
        Picture picture = new Picture();
        //查看该产品图片是否已经有默认图片
        Picture picture1 = pictureService.getOne(new QueryWrapper<Picture>().eq("product_id", productId).eq("default_picture", true), true);
        saveFileName = FileUtil.saveFile(file, uploadPath + uploadPath2 + "/picture/" + productName, uploadPath);
        pictureLink = fileHost + saveFileName;
        pictureDesc = "这是一张" + productName + "图片";
        picture.setPictureName(productName);
        picture.setPictureLink(pictureLink);
        picture.setPictureDesc(pictureDesc);
        picture.setDefaultPicture(picture1 == null);
        state = pictureService.savePicture(picture, productId);
        if(!state) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
            return ReturnResult.returnResult(false, "添加失败，请稍后重试");
        }
        return ReturnResult.returnResult(true);
    }

    //更改产品的默认图片
    @RequestMapping(value = "product/picture/updatedefault", method = RequestMethod.POST)
    public ReturnResult updateDefault(Integer pictureId) {
        // 找到对应的产品id
        Integer productId = pictureService.getOne(new QueryWrapper<Picture>().eq("picture_id", pictureId).select("product_id")).getProductId();
        // 找到这个产品下的默认图片
        Picture picture = pictureService.getOne(new QueryWrapper<Picture>().eq("product_id", productId).eq("default_picture", true).select("picture_id"), true);
        if (picture == null) {
            return ReturnResult.returnResult(false, "服务器错误");
        }

        // 将原默认图片改为false
        boolean update1 = pictureService.update(new UpdateWrapper<Picture>().eq("picture_id", picture.getPictureId()).set("default_picture", false));
        if (!update1) {
            return ReturnResult.returnResult(false, "修改失败");
        }
        boolean update2 = pictureService.update(new UpdateWrapper<Picture>().eq("picture_id", pictureId).set("default_picture", true));
        return ReturnResult.returnResult(update2);

    }

    /**
     * @description: 删除某个产品的某个图片
     * @param id:图片id
     */
    @RequestMapping(value = "product/picture/delete", method = RequestMethod.POST)
    public ReturnResult deleteProductPicture(Integer id) {
        Picture picture;
        String pictureLink;
        boolean deleteFile;
        picture = pictureService.getPictureById(id);
        pictureLink = picture.getPictureLink();
        deleteFile = FileUtil.deleteFile(uploadPath + pictureLink.substring(fileHost.length()));
        if (!deleteFile) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
            return ReturnResult.returnResult(false, "删除失败");
        }
        boolean removeByIds = pictureService.removeById(id);
        return ReturnResult.returnResult(removeByIds);
    }


    /**
     * @description: 产品详情-添加图片
     */
    @RequestMapping(value = "product/detail/uploadpicture", method = RequestMethod.POST)
    public JSONObject uploadPicture(@RequestParam(value = "file") MultipartFile file) {
        String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String saveFileName, link;
        saveFileName = FileUtil.saveFile(file, uploadPath + uploadPath2 + "/productdetail/picture/" +  date, uploadPath);
        link = fileHost + saveFileName;
        JSONObject res = new JSONObject();
        JSONObject data = new JSONObject();
        res.put("code", 0);
        res.put("msg", "上传成功");
        data.put("src", link);
        res.put("data", data);
        return res;
    }

    /**
     * @description: 产品详情-添加视频
     */
    @RequestMapping(value = "product/detail/uploadvideo", method = RequestMethod.POST)
    public JSONObject uploadVideo(@RequestParam(value = "file") MultipartFile file) {
        String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String saveFileName, link;
        saveFileName = FileUtil.saveFile(file, uploadPath + uploadPath2 + "/productdetail/video/" +  date, uploadPath);
        link = fileHost + saveFileName;
        JSONObject res = new JSONObject();
        JSONObject data = new JSONObject();
        res.put("code", 0);
        res.put("msg", "上传成功");
        data.put("src", link);
        res.put("data", data);
        return res;
    }

    //产品详情-删除文件
    @RequestMapping(value = "product/detail/calldel", method = RequestMethod.POST)
    public ReturnResult calldel(String imgpath, String filepath) {
        boolean deleteFile;
        if (!"".equals(imgpath) && imgpath != null ) {
            try {
                imgpath = URLDecoder.decode(imgpath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return ReturnResult.returnResult(false);
            }
            deleteFile = FileUtil.deleteFile(uploadPath + imgpath.substring(fileHost.length()));
            if (!deleteFile) {
                return ReturnResult.returnResult(false);
            }
        }
        if (filepath != null && filepath.length() != 0) {
            deleteFile = FileUtil.deleteFile(uploadPath + filepath.substring(fileHost.length()));
            if (!deleteFile) {
                return ReturnResult.returnResult(false);
            }
        }
        return ReturnResult.returnResult(true);
    }

    /**
     * @description: 添加或修改产品详情
     */
    @RequestMapping(value = "product/detail/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveproductdetail(Product product) {
        boolean update = productService.update(new UpdateWrapper<Product>().eq("product_id", product.getProductId()).set("product_detail", product.getProductDetail()));
        return ReturnResult.returnResult(update);
    }

    @RequestMapping(value = "test", method = RequestMethod.POST)
    public void test(@RequestParam(value = "file") MultipartFile file) throws IOException {
        InputStream ips = null;
        File file1 = File.createTempFile("temp", null);
        file.transferTo(file1);
        ips = new FileInputStream(file1);
        FileType fileType = FileUtil.getFileType(ips);
        System.out.println("++++++++++++++");
        System.out.println(fileType);
        System.out.println("++++++++++++++");
    }

}
