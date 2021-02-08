package cn.zmdxd.xddesign.admin.handler;

import cn.zmdxd.xddesign.admin.service.*;
import cn.zmdxd.xddesign.entity.*;
import cn.zmdxd.xddesign.utils.FileUtil;
import cn.zmdxd.xddesign.utils.ReturnResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;
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

    @Value("${upload_path}")
    private String uploadPath;
    @Value("${file_host}")
    private String fileHost;

    //角色添加和修改
    @RequestMapping(value = "role/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdateRole(Role role) {
        //id存在，则为修改，否则新增
        boolean saveOrUpdate = roleService.saveOrUpdate(role);
        return ReturnResult.returnResult(saveOrUpdate);
    }

    //查看角色列表
    @RequestMapping(value = "roles")
    public List<Role> findRoles() {
        return roleService.list();
    }

    //根据id查询要修改的角色
    @RequestMapping(value = "role")
    public Role findRole(Integer id) {
        return roleService.getById(id);
    }

    //删除角色
    @RequestMapping(value = "role/del", method = RequestMethod.POST)
    public ReturnResult delRole(Integer id) {
        boolean removeById = roleService.removeById(id);
        return ReturnResult.returnResult(removeById);
    }

    //添加系统用户和修改系统用户信息
    @RequestMapping(value = "user/saveorupdate", method = RequestMethod.POST)
    public String addUser(User user) {
        return userService.saveOrUpdateUser(user);
    }

   /**
    * 根据id查询要修改的用户信息
    * @param id:要查询的用户id，没有则查询全部用户   current：当前页   size：每页的条数
    * @return 没有id时返回分页数据IPage，有id时返回单个User
    */
    @RequestMapping(value = "user")
    public Object findUser(Integer id, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        return userService.findUser(id,current,size);
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

    //根据id删除一级分类
    @RequestMapping(value = "firstlevel/delete", method = RequestMethod.POST)
    public ReturnResult deleteFirstLevel(Integer firstId) {
        boolean removeById = firstLevelService.removeById(firstId);
        return ReturnResult.returnResult(removeById);

    }

    /**
     * @description: 添加或修改二级分类信息 若secondLevel中的id为null时添加信息，否则修改信息
     * @param firstId:一级分类id
     * @param secondLevel:二级分类信息 名称、描述
     */
    @RequestMapping(value = "secondlevel/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdate(Integer firstId, SecondLevel secondLevel) {
        Boolean state;
        if (secondLevel.getSecondId() == null) {
            //新增二级分类信息
            state = secondLevelService.saveSecondLevel(firstId,secondLevel);

        }else {
            //修改信息，可供修改的项有 二级分类名称、二级分类描述
            state = secondLevelService.update(new UpdateWrapper<SecondLevel>().eq("second_id", secondLevel.getSecondId()).set("second_name",secondLevel.getSecondName()).set("second_desc",secondLevel.getSecondDesc()));
        }
        return ReturnResult.returnResult(state);
    }
//
//    @RequestMapping(value = "jsontest", method = RequestMethod.POST)
//    public void jsonTest(Property property) {
//        System.out.println(property);
//        System.out.println(property.getCommonValue().get(0));
//        System.out.println(Arrays.toString(property.getCommonValue().toArray()));
//        System.out.println(property.getCommonValue().toString());
//    }

    //根据id删除产品二级分类信息
    @RequestMapping(value = "secondlevel/delete", method = RequestMethod.POST)
    public ReturnResult deleteSecondLevel(Integer secondId) {
        boolean removeById = secondLevelService.removeById(secondId);
        return ReturnResult.returnResult(removeById);
    }

    /**
     * @description: 根据一级分类id查询二级分类列表信息
     * @param firstId:一级分类id
     * @param current:当前页数
     * @param size:每页条数
     */
    @RequestMapping(value = "secondlevels")
    public IPage<SecondLevel> findSecondLevels(Integer firstId, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "100") Integer size) {
        Page<SecondLevel> page = new Page<>(current,size);
        return secondLevelService.findSecondLevels(page, firstId);
    }

    //根据二级分类id查询二级分类信息
    @RequestMapping(value = "secondlevel")
    public SecondLevel findSecondLevel(Integer secondId) {
        SecondLevel secondLevel = secondLevelService.findSecondLevel(secondId);
        List<Property> propertyList = secondLevel.getPropertyList();
        for (Property property:propertyList) {
            String[] split = property.getCommonValue().split("，");
            List<String> commonValueList = Arrays.asList(split);
            property.setCommonValueList(commonValueList);
        }
        return secondLevel;
    }

    /**
     * @description: 在二级分类下添加或修改产品属性信息
     * @param secondId:二级分类id
     * @param property:属性信息(名称、描述、常用值)
     */
    @RequestMapping(value = "property/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdateProperty(Integer secondId, Property property) {
        boolean state;
        if (property.getPropertyId() == null) {
            //添加属性
            state = propertyService.saveProperty(secondId, property);
        }else {
            //修改属性
            state = propertyService.update(new UpdateWrapper<Property>().eq("property_id",property.getPropertyId()).set("property_name",property.getPropertyName()).set("property_desc",property.getPropertyDesc()).set("common_value",property.getCommonValue()));
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

    //根据属性id批量删除属性
    @RequestMapping(value = "property/delete",method = RequestMethod.POST)
    public ReturnResult deleteProperty(@RequestBody List<Integer> ids) {
        boolean removeById = propertyService.removeByIds(ids);
        return ReturnResult.returnResult(removeById);
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
     * @param guide:产品手册信息
     * @param file:上传的文件
     */
    @RequestMapping(value = "guide/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdateGuide(Guide guide, @RequestParam(value = "file", required = false) MultipartFile file) {
        ReturnResult result = new ReturnResult();
        int status;
        String msg;
        if (guide.getGuideId() == null) {
            if (file.getSize() != 0) {
                //添加产品手册信息
                String saveFileName = FileUtil.saveFile(file, uploadPath + "/guide/" + guide.getGuideName());//上传路径名
                String guideLink = fileHost+saveFileName;//保存到数据库中的链接
                guide.setGuideLink(guideLink);
                boolean saveOrUpdate = guideService.saveOrUpdate(guide);
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
            //修改信息
            if (file.getSize() != 0) {
                String saveFileName = FileUtil.saveFile(file, uploadPath + "/guide/" + guide.getGuideName());//上传路径名
                String guideLink = fileHost+saveFileName;//保存到数据库中的链接
                guide.setGuideLink(guideLink);
            }
            boolean update = guideService.update(new UpdateWrapper<Guide>().eq("guide_id", guide.getGuideId()).set("guide_name", guide.getGuideName()).set("guide_link", guide.getGuideLink()).set("guide_desc", guide.getGuideDesc()).set("guide_reserve", guide.getGuideReserve()));
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
     * @param productId:产品id 当id为null时，查看所有产品列表；不为null时，查看单个产品
     * @param current:当前页
     * @param size:每页条数
     */
    @RequestMapping(value = "products", method = RequestMethod.GET)
    public Object findProducts(Integer productId,@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        //由于mybatis-plus对一表对多表left join查询的支持欠缺，所以不用它的分页查询
        Page<Product> page = new Page<>(1,1000000000);
        page.setSearchCount(false);
        if (productId == null) {
            IPage<Product> iPage = productService.findProducts(page, productId, current, size);
            iPage.setSize(size);
            iPage.setTotal(productService.count(new QueryWrapper<Product>().select("product_id")));
            return iPage;
        }
        return productService.findProducts(page,productId,current,size).getRecords().get(0);
    }

    /**
     * @description: 根据二级分类查找其下的产品列表
     * @param secondId:二级分类id
     * @param current:当前页数
     * @param size:每页条数
     */
    @RequestMapping(value = "secondlevel/products", method = RequestMethod.GET)
    public IPage<Product> findProductsBySecondLevel(Integer secondId,@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<Product> page = new Page<>(1,1000000000);
        page.setSearchCount(false);
        IPage<Product> iPage = productService.findProductsBySecond(page, secondId, current, size);
        iPage.setSize(size);
        iPage.setTotal(productService.count(new QueryWrapper<Product>().select("product_id").eq("second_id",secondId)));
        return iPage;
    }

    //修改产品基本信息
    @RequestMapping(value = "product/update", method = RequestMethod.POST)
    public ReturnResult updateProduct(Product product) {
        boolean update = productService.update(new UpdateWrapper<Product>().eq("product_id", product.getProductId()).set("product_name", product.getProductName()).set("product_link", product.getProductLink()).set("product_models", product.getProductModels()).set("product_desc", product.getProductDesc()).set("price", product.getPrice()).set("product_reserve1", product.getProductReserve1()).set("product_reserve2", product.getProductReserve2()).set("product_reserve3", product.getProductReserve3()).set("product_reserve4", product.getProductReserve4()).set("product_reserve5", product.getProductReserve5()));
        return ReturnResult.returnResult(update);
    }

    //批量删除产品
    @RequestMapping(value = "product/delete", method = RequestMethod.POST)
    public ReturnResult deleteProduct(@RequestBody List<Integer> idList) {
        boolean removeByIds = productService.removeByIds(idList);
        return ReturnResult.returnResult(removeByIds);
    }

    //修改产品某个属性信息
    @RequestMapping(value = "product/property/update", method = RequestMethod.POST)
    public ReturnResult updateProperty(PropertyValue propertyValue) {
        boolean state = valueService.update(new UpdateWrapper<PropertyValue>().eq("value_id",propertyValue.getValueId()).set("value_name",propertyValue.getValueName()));
        return ReturnResult.returnResult(state);
    }

    //删除产品某个属性
    @RequestMapping(value = "/product/property/delete")
    public ReturnResult deleteProperty(PropertyValue propertyValue) {
        ReturnResult result = new ReturnResult();
        boolean state2 = productService.removeProductProperty(propertyValue.getValueId());
        boolean state1 = valueService.removeById(propertyValue.getValueId());
        System.out.println(state1+""+state2);
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
