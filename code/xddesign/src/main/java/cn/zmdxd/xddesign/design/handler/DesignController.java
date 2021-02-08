package cn.zmdxd.xddesign.design.handler;

import cn.zmdxd.xddesign.design.service.*;
import cn.zmdxd.xddesign.entity.*;
import cn.zmdxd.xddesign.utils.CookieUtil;
import cn.zmdxd.xddesign.utils.EnumUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author 曹涛
 * @date 2021/1/19 9:51
 * @description: 设计人员模块
 */
@RestController
@PropertySource(value = "classpath:config.properties")
@RequestMapping(value = "/design/", produces = { "application/json;charset=UTF-8;" })
@Transactional
public class DesignController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private HouseService houseService;
    @Autowired
    private SolutionsService solutionsService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ProductNumService productNumService;
    @Autowired
    private QuoteService quoteService;//方案报价单接口
    @Autowired
    private RenderingsService renderingsService;//效果图接口
    @Autowired
    private HouseTypeService houseTypeService;//户型接口
    @Autowired
    private TemplateService templateService;

    @Value("${upload_path}")
    private String uploadPath;
    @Value("${file_host}")
    private String fileHost;

    /**
     * @description: 添加或修改客户信息，customer中id若为null，则添加客户信息，添加时区分是管理员添加还是设计人员添加；否则修改客户信息
     * @param customer:客户信息(客户名称、客户电话、客户需求、客户地址、客户类别)
     */
    @RequestMapping(value = "customer/saveOrUpdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdateCustomer(Customer customer, HttpServletRequest request) {
        //设计人员添加
        if ("设计人员".equals(CookieUtil.getCookieValue(request,"roleName","UTF-8"))) {
            User user = new User();
            user.setId(Integer.valueOf(CookieUtil.getCookieValue(request, "userId")));
            customer.setDesign(user);
        }
        //管理员添加客户时指定由哪个设计人员负责该客户
        return customerService.saveOrUpdateCustomer(customer);

    }

   /**
    * @description: 分页查询客户列表
    * @param current:当前页
    * @param size:每页条数
    * @return IPage:分页对象
    */
    @RequestMapping("customers")
    public IPage<Customer> findCustomers(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "1") Integer size, HttpServletRequest request) {
        Page<Customer> page = new Page<>(current,size);
        Integer designId = null;
        if ("设计人员".equals(CookieUtil.getCookieValue(request,"roleName","utf-8"))) {
            designId = Integer.valueOf(CookieUtil.getCookieValue(request, "userId"));
        }
        return customerService.findCustomers(page, designId);
    }

    //不分页查询全部客户
    @RequestMapping(value = "customers/nopage")
    public List<Customer> findCustomerList(HttpServletRequest request) {
        Integer designId = Integer.valueOf(CookieUtil.getCookieValue(request, "userId"));
        return customerService.list(new QueryWrapper<Customer>().eq("design_id", designId).select("id","username"));
    }

    //根据id查询客户信息
    @RequestMapping("customer")
    public Customer findCustomer(Integer id) {
        return customerService.findCustomer(id);
    }

    //批量删除客户
    @RequestMapping(value = "customer/delete",method = RequestMethod.POST)
    public ReturnResult deleteCustomer(@RequestBody List<Integer> idList) {
//        boolean removeByIds = customerService.removeByIds(idList);
//        return ReturnResult.returnResult(removeByIds);
        Customer customer;
        boolean removeById;
        ReturnResult result;
        for (Integer id:idList) {
            customer = customerService.findCustomer(id);
            List<House> houseList = customer.getHouseList();
            for (House house:houseList) {
                result = deleteHouse(house.getHouseId());
                if (result.getStatus() == 0) return result;
            }
            removeById = customerService.removeById(id);
            if (!removeById) return ReturnResult.returnResult(false);
        }
        return ReturnResult.returnResult(true);
    }

    //查询户型列表
    @RequestMapping(value = "customer/houseType")
    public List<HouseType> findHouseTypeList() {
        return houseTypeService.list(new QueryWrapper<HouseType>().orderByDesc("type_num"));
    }

    /**
     * @description: 添加或修改客户房子信息，当house中的id为null时添加/不为null时修改
     * @param customerId:客户id
     * @param house:房子信息(房子名称、房子地址)
     */
    @RequestMapping(value = "customer/house/saveorupdate")
    public ReturnResult saveOrUpdateHouse(Integer customerId, House house) {
        ReturnResult result = new ReturnResult();
        Integer typeId;
        HouseType houseType = houseTypeService.getOne(new QueryWrapper<HouseType>().eq("type_name", house.getHouseType().getTypeName()), true);
        if (houseType == null) {
            HouseType houseType1 = new HouseType();
            houseType1.setTypeName(house.getHouseType().getTypeName());
            houseType1.setTypeNum(1);
            boolean save = houseTypeService.save(houseType1);
            if (!save) {
                result.setStatus(0);
                result.setMsg("添加失败");
                return result;
            }
            house.setHouseType(houseType1);
            typeId = houseType1.getTypeId();
        }else {
            houseType.setTypeNum(houseType.getTypeNum() + 1);
            boolean updateById = houseTypeService.updateById(houseType);
            if (!updateById) {
                result.setStatus(0);
                result.setMsg("添加失败");
                return result;
            }
            house.setHouseType(houseType);
            typeId = houseType.getTypeId();
        }

        if (house.getHouseId() == null) {
            //新增房子信息
            house.setCustomerId(customerId);
            return houseService.saveHouse(house);

        }else {
            //修改房子信息 可供修改的项有：房子名称、房子地址、房子户型
            boolean update = houseService.update(new UpdateWrapper<House>().eq("house_id", house.getHouseId()).set("house_name", house.getHouseName()).set("house_address", house.getHouseAddress()).set("type_id", typeId).set("house_reserve1",house.getHouseReserve1()).set("house_reserve2",house.getHouseReserve2()).set("house_reserve3",house.getHouseReserve3()));
            return ReturnResult.returnResult(update);

        }
    }

    //删除客户房子信息
    @RequestMapping(value = "customer/house/delete",method = RequestMethod.POST)
    public ReturnResult deleteHouse(Integer houseId) {
        //检查该房子是否关联了模板方案
        if (templateService.findTemplateByHouseId(houseId) != null) {
            ReturnResult result = new ReturnResult();
            result.setStatus(0);
            result.setMsg("关联了模板方案，不能删除");
            return result;
        }
        boolean removeById = houseService.removeById(houseId);
        return ReturnResult.returnResult(removeById);
    }

    //根据户型查询模板方案
    @RequestMapping(value = "customer/houseType/templates")
    public List<Template> findTemplateList(Integer typeId) {
        return templateService.list(new QueryWrapper<Template>().select("temp_id","temp_name","solu_id").eq("type_id",typeId));
    }

    /**
     * @description: 分页查询公共模板方案
     * @param current:当前页
     * @param size:每页条数
     * @return IPage
     */
    @RequestMapping(value = "customer/templates")
    public IPage<Template> findTemplateList(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<Template> page = new Page<>(current,size);
        return templateService.findTemplateList(page);
    }

    //根据id查询模板方案详情
    @RequestMapping(value = "customer/template")
    public Template findTemplateById(Integer tempId) {
        return templateService.findTemplateById(tempId);
    }

    //根据id删除模板方案
    @RequestMapping(value = "customer/template/delete", method = RequestMethod.POST)
    public ReturnResult deleteTemplate(Integer tempId, HttpServletRequest request) {

        //修改方案状态为未共享
        Template template = templateService.findTemplateById(tempId);
        Integer soluId = template.getSolutions().getSoluId();
        solutionsService.update(new UpdateWrapper<Solutions>().eq("solu_id",soluId).set("share_sign",false));

        Integer designId = null;
        if ("设计人员".equals(CookieUtil.getCookieValue(request,"roleName"))) {
            designId = Integer.valueOf(CookieUtil.getCookieValue(request,"userId"));
        }
        boolean remove;
        if (designId != null) {
            //设计人员删除
            remove = templateService.remove(new QueryWrapper<Template>().eq("temp_id", tempId).eq("designer_id", designId));
        }else {
            //管理员删除
            remove = templateService.remove(new QueryWrapper<Template>().eq("temp_id", tempId));
        }
        return ReturnResult.returnResult(remove);
    }

    //我的方案转模板方案
    @RequestMapping(value = "customer/template/save", method = RequestMethod.POST)
    public ReturnResult solutionsToTemplate(Template template,HttpServletRequest request) {
        Integer typeId = templateService.findTypeIdBySoluId(template.getSolutions().getSoluId());//得到户型id
        Integer designId = Integer.valueOf(CookieUtil.getCookieValue(request, "userId"));//得到设计人员或管理员id
        boolean saveTemplate = templateService.saveTemplate(template, typeId, designId);

        //修改方案状态为已共享
        Integer soluId = template.getSolutions().getSoluId();
        solutionsService.update(new UpdateWrapper<Solutions>().eq("solu_id",soluId).set("share_sign",true));
        return ReturnResult.returnResult(saveTemplate);
    }

    //修改模板方案
    @RequestMapping(value = "customer/template/update", method = RequestMethod.POST)
    public ReturnResult updateTemplate(Template template) {
        //可供修改的项有模板方案名称和描述
        boolean updateById = templateService.updateById(template);
        return ReturnResult.returnResult(updateById);
    }

    /**
     * @description: 快速建方案
     * @param templateVo:模板方案Vo类 (客户名称、客户电话、客户联系地址、客户户型id、根据户型匹配的方案id)
     * @return ReturnResult
     */
    @RequestMapping(value = "customer/solutions/quicksave")
    public ReturnResult quickSaveSolutions(TemplateVo templateVo, HttpServletRequest request) {
        Customer customer = customerService.getOne(new QueryWrapper<Customer>().select("id").eq("mobile", templateVo.getMobile()), true);//根据手机号查询客户存在与否
        if (customer == null) {
            customer = new Customer();
            User design = new User();
            customer.setUsername(templateVo.getUsername());
            customer.setMobile(templateVo.getMobile());
            customer.setAddress(templateVo.getAddress());
            Integer designId = Integer.valueOf(CookieUtil.getCookieValue(request, "userId"));
            design.setId(designId);
            customer.setDesign(design);
            ReturnResult result = customerService.saveOrUpdateCustomer(customer);//添加客户
            if (result.getStatus()==0) return result;
        }
        Integer customerId = customer.getId();//得到客户id
        House house = new House();
        house.setCustomerId(customerId);
        HouseType houseType = new HouseType();
        houseType.setTypeId(templateVo.getTypeId());
        house.setHouseName("客户"+templateVo.getUsername()+"的家");
        house.setHouseAddress(templateVo.getAddress());
        house.setHouseType(houseType);
        ReturnResult result = houseService.saveHouse(house);//添加客户房子
        if (result.getStatus() == 0) return result;
        Integer houseId = house.getHouseId();//得到房子id

        //根据方案id查询方案详情
        Solutions solutions = solutionsService.findSolutions(templateVo.getSoluId());
        solutions.setHouseId(houseId);
        solutions.setSoluId(null);
        solutions.setSoluName("客户"+templateVo.getUsername()+"的快速方案");
        solutions.setSoluDesc("客户"+templateVo.getUsername()+"的快速方案描述");
        solutions.setState(SolutionsStateEnum.DESIGNING.getMsg());
        List<Room> roomList = solutions.getRoomList();
        List<ProductNum> productNumList;
        for (Room room:roomList) {
            room.setRoomId(null);
            productNumList = room.getProductNumList();
            for (ProductNum productNum:productNumList) {
                productNum.setPnId(null);
            }
        }
        //快速建方案
        return solutionsService.saveOrUpdateSolution(solutions, request);
    }

    /**
     * @description: 添加和修改方案信息-自定义方案
     * (当solutions中的solu_id为null时，直接添加方案;不为null时，先删除方案，数据库中设置的级联删除，所以这个方案的房间信息、房间内的产品信息、报价单信息也一并删除，然后再重新添加方案)
     * @param solutions:要添加的方案信息 (房子id、方案名称、方案描述、房间名称、产品id、产品数量)
     */
    @RequestMapping(value = "customer/solutions/saveorupdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdateSolutions(@RequestBody Solutions solutions, HttpServletRequest request) {
        return solutionsService.saveOrUpdateSolution(solutions,request);
    }

    //方案状态列表
    @RequestMapping(value = "solutions/state")
    public List<Map<String, Object>> getSolutionsState() {
        return EnumUtil.enumToListMap(SolutionsStateEnum.class);
    }

    /**
     * @description: 修改方案状态
     * @param soluId:方案id
     * @param code:方案状态码
     */
    @RequestMapping(value = "solutions/state/update", method = RequestMethod.POST)
    public ReturnResult updateSolutionsState(Integer soluId, Integer code) {
        String state = EnumUtil.getMsgByCode(SolutionsStateEnum.class,code);
        boolean update = solutionsService.update(new UpdateWrapper<Solutions>().eq("solu_id", soluId).set("state", state));
        return ReturnResult.returnResult(update);
    }

    //删除客户方案信息（连同这个方案的房间、产品信息一并删除）
    @RequestMapping(value = "customer/solutions/delete",method = RequestMethod.POST)
    public ReturnResult deleteSolutions(Integer soluId) {
        if (templateService.findTemplateBySoluId(soluId) != null) {
            ReturnResult result = new ReturnResult();
            result.setStatus(0);
            result.setMsg("该方案关联了模板方案，不能删除");
            return result;
        }
        return ReturnResult.returnResult(solutionsService.removeById(soluId));
    }

    //分页查询方案列表
    @RequestMapping(value = "customer/solutions")
    public IPage<Solutions> findSolutions(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        //管理员的designId为null
        Integer designId = null;
        if ("设计人员".equals(CookieUtil.getCookieValue(request,"roleName","utf-8"))) {
            designId = Integer.valueOf(CookieUtil.getCookieValue(request, "userId"));
        }
        Page<Solutions> page = new Page<>(current,size);
        return solutionsService.findSolutionsList(page,designId);
    }

    //根据id查询单条报价数据
    @RequestMapping(value = "customer/quote")
    public Quote findQuote(Integer quoteId) {
        return quoteService.getById(quoteId);
    }

    //根据id修改单条报价数据
    @RequestMapping(value = "customer/quote/update",method = RequestMethod.POST)
    public ReturnResult updateQuote(Quote quote) {
        boolean update = quoteService.update(quote, new UpdateWrapper<Quote>().eq("quote_id", quote.getQuoteId()));
        return ReturnResult.returnResult(update);
    }

    //根据id删除方案内的单条报价数据
    @RequestMapping(value = "customer/quote/delete", method = RequestMethod.POST)
    public ReturnResult deleteQuote(Integer quoteId) {
        boolean removeById = quoteService.removeById(quoteId);
        return ReturnResult.returnResult(removeById);
    }

    /**
     * @description: 上传方案效果图
     * @param soluId:方案id
     * @param soluName:方案名称
     * @param files:上传的图片
     * @return ReturnResult
     */
    @RequestMapping(value = "customer/renderings/save", method = RequestMethod.POST)
    public ReturnResult saveRenderings(Integer soluId, String soluName, @RequestParam(value = "files",required = false) MultipartFile[] files) {
        ReturnResult result = new ReturnResult();
        String saveFileName,rendPath,rendDesc,rendName;
        boolean save;
        Renderings renderings = new Renderings();
        for (MultipartFile file:files) {
            if (file.getSize() == 0) {
                result.setStatus(0);
                result.setMsg("没有上传任何图片");
                return result;
            }
            saveFileName = FileUtil.saveFile(file,uploadPath+"/renderings/"+soluName);
            rendPath = fileHost+saveFileName;
            rendDesc = "这是"+soluName+"的效果图";
            rendName = Objects.requireNonNull(file.getOriginalFilename()).substring(0, file.getOriginalFilename().lastIndexOf("."));
            renderings.setRendName(rendName);
            renderings.setRendDesc(rendDesc);
            renderings.setRendPath(rendPath);
            renderings.setSoluId(soluId);
            save = renderingsService.save(renderings);
            if (!save) {
                result.setStatus(0);
                result.setMsg("上传失败，请稍后重试");
                return result;
            }
        }
        result.setMsg("上传成功");
        result.setStatus(1);
        return result;

    }

    /**
     * @description: 批量删除效果图
     * @param idList:id集合
     * @return ReturnResult
     */
    @RequestMapping(value = "customer/renderings/delete", method = RequestMethod.POST)
    public ReturnResult deleteRenderings(@RequestBody List<Integer> idList) {
        ReturnResult result = new ReturnResult();
        String path;
        boolean deleteFile;
        List<Renderings> renderingsList = renderingsService.listByIds(idList);
        for (Renderings renderings:renderingsList) {
            path = uploadPath + renderings.getRendPath().substring(fileHost.length());
            deleteFile = FileUtil.deleteFile(path);
            if (!deleteFile) {
                result.setStatus(0);
                result.setMsg("删除失败，请稍后重试");
                return result;
            }
        }
        boolean removeByIds = renderingsService.removeByIds(idList);
        return ReturnResult.returnResult(removeByIds);
    }


}
