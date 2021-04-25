package cn.zmdxd.xddesign.design.handler;

import cn.zmdxd.xddesign.admin.service.FirstLevelService;
import cn.zmdxd.xddesign.admin.service.ProductService;
import cn.zmdxd.xddesign.admin.service.SecondLevelService;
import cn.zmdxd.xddesign.design.service.*;
import cn.zmdxd.xddesign.entity.*;
import cn.zmdxd.xddesign.util.CookieUtil;
import cn.zmdxd.xddesign.util.EnumUtil;
import cn.zmdxd.xddesign.util.FileUtil;
import cn.zmdxd.xddesign.util.ReturnResult;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
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
    private QuoteService quoteService;//方案报价单接口
    @Autowired
    private RenderingsService renderingsService;//效果图接口
    @Autowired
    private HouseTypeService houseTypeService;//户型接口
    @Autowired
    private TemplateService templateService;
    @Autowired
    private FirstLevelService firstLevelService;
    @Autowired
    private SecondLevelService secondLevelService;
    @Autowired
    private ProductService productService;

    @Value("${upload_path}")
    private String uploadPath;
    @Value("${file_host}")
    private String fileHost;
    @Value("${upload_path_2}")
    private String uploadPath2;

    /**
     * @description: 添加或修改客户信息，customer中id若为null，则添加客户信息，添加时区分是管理员添加还是设计人员添加；否则修改客户信息
     * @param customer:客户信息(客户名称、客户电话、客户需求、客户地址、客户类别)
     */
    @RequestMapping(value = "customer/saveOrUpdate", method = RequestMethod.POST)
    public ReturnResult saveOrUpdateCustomer(Customer customer, HttpServletRequest request) {
        // 设计人员添加
        if ("设计人员".equals(CookieUtil.getCookieValue(request,"roleName","UTF-8"))) {
            User user = new User();
            user.setId(Integer.valueOf(CookieUtil.getCookieValue(request, "userId")));
            customer.setDesign(user);
        }
        // 管理员添加客户时指定由哪个设计人员负责该客户
        ReturnResult result = customerService.saveOrUpdateCustomer(customer);
        result.setId(customer.getId());
        return result;

    }

    // 客户类别列表
    @RequestMapping(value = "customer/category")
    public List<Map<String,Object>> findCustomerCategory() {
        return EnumUtil.enumToListMap(CustomerEnum.class);
    }

   /**
    * @description: 分页查询客户列表
    * @param current:当前页
    * @param size:每页条数
    * @return IPage:分页对象
    */
    @RequestMapping("customers")
    public IPage<Customer> findCustomers(Customer customer, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size, HttpServletRequest request) {
        Page<Customer> page = new Page<>(current,size);
        int designId;
        User design = new User();
        if ("设计人员".equals(CookieUtil.getCookieValue(request,"roleName","utf-8"))) {
            designId = Integer.parseInt(CookieUtil.getCookieValue(request, "userId"));
            design.setId(designId);
            customer.setDesign(design);
        }
        return customerService.findCustomers(page, customer);
    }

    // 不分页查询全部客户
    @RequestMapping(value = "customers/nopage")
    public List<Customer> findCustomerList(HttpServletRequest request) {
        if ("设计人员".equals(CookieUtil.getCookieValue(request, "roleName", "utf-8"))) {
            Integer designId = Integer.valueOf(CookieUtil.getCookieValue(request, "userId"));
            return customerService.list(new QueryWrapper<Customer>().eq("design_id", designId).select("id", "username"));
        }else if ("管理员".equals(CookieUtil.getCookieValue(request, "roleName", "utf-8")))
            return customerService.list(new QueryWrapper<Customer>().select("id", "username"));
         else   //这里先暂时这样处理，方便测试
            return customerService.list(new QueryWrapper<Customer>().select("id", "username"));
    }

    // 根据id查询客户信息
    @RequestMapping("customer")
    public Customer findCustomer(Integer id) {
        return customerService.findCustomer(id);
    }

    // 删除客户
    @RequestMapping(value = "customer/del", method = RequestMethod.POST)
    public ReturnResult delCustomer(Integer id) {
//        ReturnResult result;
//        Customer customer = customerService.findCustomer(id);
//        List<House> houseList = customer.getHouseList();
//        if (houseList.get(0).getHouseId() != null) {
//            for (House house : houseList) {
//                result = deleteHouse(house.getHouseId());
//                if (result.getStatus() == 0) return result;
//            }
//        }
//        boolean removeById = customerService.removeById(id);

        // 假删除
        boolean update = customerService.update(new UpdateWrapper<Customer>().eq("id", id).set("del_sign", true));
        return ReturnResult.returnResult(update);
    }

    // 批量删除客户
    @RequestMapping(value = "customer/batchdel",method = RequestMethod.POST)
    public ReturnResult deleteCustomer(@RequestBody List<Customer> customerList) {
//        boolean removeById;
//        for (Customer customer:customerList) {
//            removeById = customerService.removeById(customer.getId());
//            if (!removeById) return ReturnResult.returnResult(false);
//        }
//        return ReturnResult.returnResult(true);

        for (Customer customer:customerList) {
            boolean update = customerService.update(new UpdateWrapper<Customer>().eq("id", customer.getId()).set("del_sign", true));
            if (!update) {
                // 事务回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ReturnResult.returnResult(false);
            }
        }
        return ReturnResult.returnResult(true);
    }

    // 查询户型列表
    @RequestMapping(value = "customer/houseType")
    public List<HouseType> findHouseTypeList() {
        return houseTypeService.list(new QueryWrapper<HouseType>().orderByDesc("type_num"));
    }

    /**
     * @description: 添加或修改客户房子信息，当house中的id为null时添加/不为null时修改
     * @param house:房子信息(房子名称、房子地址)
     */
    @RequestMapping(value = "customer/house/saveorupdate")
    public ReturnResult saveOrUpdateHouse(@RequestBody House house) {
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
                // 事务回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
                // 事务回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result;
            }
            house.setHouseType(houseType);
            typeId = houseType.getTypeId();
        }

        if (house.getHouseId() == null) {
            // 新增房子信息
            ReturnResult saveHouse = houseService.saveHouse(house);
            saveHouse.setId(house.getHouseId());
            return saveHouse;

        }else {
            // 修改房子信息 可供修改的项有：房子名称、房子地址、房子户型
            boolean update = houseService.update(new UpdateWrapper<House>().eq("house_id", house.getHouseId()).set("house_name", house.getHouseName()).set("house_address", house.getHouseAddress()).set("type_id", typeId).set("house_reserve1",house.getHouseReserve1()).set("house_reserve2",house.getHouseReserve2()).set("house_reserve3",house.getHouseReserve3()));
            ReturnResult returnResult = ReturnResult.returnResult(update);
            returnResult.setId(house.getHouseId());
            return returnResult;
        }
    }

    // 查询客户房子列表
    @RequestMapping(value = "customer/house")
    public IPage<House> findHouse(House house, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<House> page = new Page<>(current, size);
        Customer customer = house.getCustomer();
        if (customer != null) {
            if (customer.getUsername().trim().equals("")) {
                house.setCustomer(null);
            } else {
                customer.setUsername(customer.getUsername().trim());
            }
        }
//        if ("设计人员".equals(CookieUtil.getCookieValue(request, "roleName", "utf-8"))) {
//
//        }
        return houseService.findHouse(page, house);
    }

    // 根据客户id查询客户房子列表
    @RequestMapping(value = "customer/house/nopage")
    public List<House> findHouse(Integer customerId) {
        return houseService.list(new QueryWrapper<House>().eq("customer_id", customerId).select("house_id", "house_name"));
    }
    // 根据房子id查询客户方案列表
    @RequestMapping(value = "customer/solutions/nopage")
    public List<Solutions> findSolutions(Integer houseId) {
        return solutionsService.list(new QueryWrapper<Solutions>().eq("house_id", houseId).select("solu_id", "solu_name"));
    }
    // 根据方案id查询房间列表
    @RequestMapping(value = "customer/room/nopage")
    public List<Room> findRoom(Integer soluId) {
        return roomService.list(new QueryWrapper<Room>().eq("solu_id", soluId).select("room_id", "room_name"));
    }

    // 批量删除房子信息
    @RequestMapping(value = "customer/house/batchdel", method=RequestMethod.POST)
    public ReturnResult deleteHouse(@RequestBody List<House> houseList) {
        for (House house:houseList) {
            ReturnResult result = deleteHouse(house.getHouseId());
            if (result.getStatus() == 0) {
                // 事务回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ReturnResult.returnResult(false, result.getMsg());
            }
        }
        return ReturnResult.returnResult(true);
    }

    // 删除客户房子信息
    @RequestMapping(value = "customer/house/del",method = RequestMethod.POST)
    public ReturnResult deleteHouse(Integer houseId) {
        // 检查该房子是否关联了模板方案
        if (templateService.findTemplateByHouseId(houseId) != null) {
            return ReturnResult.returnResult(false, "关联了模板方案，不能删除");
        }
        boolean removeById = houseService.removeById(houseId);
        return ReturnResult.returnResult(removeById);
    }

    // 根据户型查询模板方案
    @RequestMapping(value = "customer/houseType/templates")
    public List<Template> findTemplateList(Integer typeId) {
        return templateService.findTemplateByTypeId(typeId);
    }

    /**
     * @description: 分页查询模板方案
     * @param current:当前页
     * @param size:每页条数
     * @return IPage
     */
    @RequestMapping(value = "customer/templates")
    public IPage<Template> findTemplateList(Template template, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<Template> page = new Page<>(current,size);
        return templateService.findTemplateList(page, template);
    }

    // 根据id查询模板方案详情
    @RequestMapping(value = "customer/template")
    public Template findTemplateById(Integer tempId) {
        return templateService.findTemplateById(tempId);
    }

    // 根据id删除模板方案
    @RequestMapping(value = "customer/template/delete", method = RequestMethod.POST)
    public ReturnResult deleteTemplate(Integer tempId, Integer userId, HttpServletRequest request) {
        Integer designId = null;
        if (CookieUtil.getCookieValue(request, "userId") == null) {
            return ReturnResult.returnResult(false, "服务器出错啦");
        }else {
            if ("设计人员".equals(CookieUtil.getCookieValue(request,"roleName"))) {
                designId = Integer.valueOf(CookieUtil.getCookieValue(request,"userId"));
                if (!Integer.valueOf(CookieUtil.getCookieValue(request, "userId")).equals(userId)) {
                    return ReturnResult.returnResult(false, "不是你的模板，不能删除");
                }
            }
        }

        // 修改方案状态为未共享
        Template template = templateService.findTemplateById(tempId);
        Integer soluId = template.getSolutions().getSoluId();
        solutionsService.update(new UpdateWrapper<Solutions>().eq("solu_id",soluId).set("share_sign",false));
        boolean remove;
        if (designId != null) {
            // 设计人员删除
            remove = templateService.remove(new QueryWrapper<Template>().eq("temp_id", tempId).eq("designer_id", designId));
        }else {
            // 管理员删除
            remove = templateService.remove(new QueryWrapper<Template>().eq("temp_id", tempId));
        }
        return ReturnResult.returnResult(remove);
    }

    @RequestMapping(value = "customer/template/batchdelete", method = RequestMethod.POST)
    public ReturnResult deleteTemplate(@RequestBody ArrayList<Integer> ids) {
        Template template;
        Integer soluId;
        boolean remove;
        for (Integer id:ids) {
            template = templateService.findTemplateById(id);
            soluId = template.getSolutions().getSoluId();
            solutionsService.update(new UpdateWrapper<Solutions>().eq("solu_id",soluId).set("share_sign",false));
            remove = templateService.remove(new QueryWrapper<Template>().eq("temp_id", id));
            if (!remove) {
                return ReturnResult.returnResult(false);
            }
        }
        return ReturnResult.returnResult(true);
    }

    //我的方案转模板方案
    @RequestMapping(value = "customer/template/save", method = RequestMethod.POST)
    public ReturnResult solutionsToTemplate(Template template,HttpServletRequest request) {
        Solutions solutions = solutionsService.getById(template.getSolutions().getSoluId());
        if (solutions.getShareSign()) {
            return ReturnResult.returnResult(false, "已经是模板方案啦");
        }
        // 得到户型id
        Integer typeId = templateService.findTypeIdBySoluId(template.getSolutions().getSoluId());
        // 得到设计人员或管理员id
        Integer designId = Integer.valueOf(CookieUtil.getCookieValue(request, "userId"));
        boolean saveTemplate = templateService.saveTemplate(template, typeId, designId);

        // 修改方案状态为已共享
        Integer soluId = template.getSolutions().getSoluId();
        solutionsService.update(new UpdateWrapper<Solutions>().eq("solu_id",soluId).set("share_sign",true));
        return ReturnResult.returnResult(saveTemplate);
    }

    // 修改模板方案
    @RequestMapping(value = "customer/template/update", method = RequestMethod.POST)
    public ReturnResult updateTemplate(Template template) {
        // 可供修改的项有模板方案名称和描述
        boolean updateById = templateService.updateById(template);
        return ReturnResult.returnResult(updateById);
    }

    /**
     * @description: 快速建方案
     * @param templateVo:模板方案Vo类 (客户名称、客户电话、客户联系地址、客户户型id、根据户型匹配的方案id)
     * @return ReturnResult
     */
    @RequestMapping(value = "customer/solutions/quicksave", method = RequestMethod.POST)
    public ReturnResult quickSaveSolutions(TemplateVo templateVo, HttpServletRequest request) {
        if (templateVo.getTypeId() == 0) {
            return ReturnResult.returnResult(false, "选择一个户型以匹配模板方案");
        }
        if (templateVo.getSoluId() == null) {
            return ReturnResult.returnResult(false, "该户型没有匹配的方案");
        }
        // 根据手机号查询客户存在与否
        Customer customer = customerService.getOne(new QueryWrapper<Customer>().select("id", "username").eq("mobile", templateVo.getMobile()), true);
        String cusName;
        if (customer == null) {
            customer = new Customer();
            User design = new User();
            customer.setUsername(templateVo.getUsername());
            cusName = templateVo.getUsername();
            customer.setMobile(templateVo.getMobile());
            customer.setAddress(templateVo.getAddress());
            Integer designId = Integer.valueOf(CookieUtil.getCookieValue(request, "userId"));
            design.setId(designId);
            customer.setDesign(design);
            ReturnResult result = customerService.saveOrUpdateCustomer(customer);//添加客户
            if (result.getStatus()==0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
                return result;
            }
        }else {
            // 还用原来的名称
            cusName = customer.getUsername();
        }
        // 得到客户id
        Integer customerId = customer.getId();
        House house = new House();
        house.setCustomerId(customerId);
        HouseType houseType = new HouseType();
        houseType.setTypeId(templateVo.getTypeId());
        house.setHouseName("客户" + cusName + "的" + templateVo.getTypeName() + "的家");
        house.setHouseAddress(templateVo.getAddress());
        house.setHouseType(houseType);
        // 添加客户房子
        ReturnResult result = houseService.saveHouse(house);
        if (result.getStatus() == 0) {
            // 事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }
        // 得到房子id
        Integer houseId = house.getHouseId();

        // 根据方案id查询方案详情
        Solutions solutions = solutionsService.findSolutions(templateVo.getSoluId());
        solutions.setHouseId(houseId);
        solutions.setSoluId(null);
        solutions.setSoluName("客户"+cusName+"的快速方案");
        solutions.setSoluDesc("客户"+cusName+"的快速方案描述");
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
        // 快速建方案
        ReturnResult saveOrUpdateSolution = solutionsService.saveOrUpdateSolution(solutions, request);
        if (saveOrUpdateSolution.getStatus() == 0) {
            // 事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnResult.returnResult(false);
        }else {
            // 复制效果图
            renderingsService.copyRenderings(templateVo.getSoluId(), solutions.getSoluId(), cusName);
            return ReturnResult.returnResult(true);
        }
    }

    // 添加方案时对应的左侧分类栏
    @RequestMapping(value = "customer/solutions/levels")
    public List<List<SecondLevel>> findFirstLevelAndSecondLevel() {
        List<FirstLevel> firstLevelList = firstLevelService.list(new QueryWrapper<FirstLevel>().select("first_id", "first_name"));
        List<SecondLevel> secondLevelList;
        List<List<SecondLevel>> levelList = new ArrayList<>();
        for (FirstLevel firstLevel:firstLevelList) {
            secondLevelList = secondLevelService.list(new QueryWrapper<SecondLevel>().eq("first_id", firstLevel.getFirstId()).select("second_id", "second_name"));
            for (SecondLevel secondLevel:secondLevelList) {
                secondLevel.setFirstLevel(firstLevel);
            }
            levelList.add(secondLevelList);
        }
        return levelList;
    }

    // 根据二级分类查询对应产品列表
    public List<Product> findProductBySecondId(Integer secondId) {
        return productService.findProductsBySecond(secondId);
    }

    // 查询全部二级分类下的全部产品
    @RequestMapping(value = "customer/solutions/level/products")
    public List<Map<SecondLevel, List<Product>>> findAllProducts() {
        List<List<SecondLevel>> listLeftNav = findFirstLevelAndSecondLevel();
        List<Map<SecondLevel, List<Product>>> list = new ArrayList<>();
        Map<SecondLevel, List<Product>> map = new LinkedHashMap<>();
        List<Product> listProduct;
        for (List<SecondLevel> listScdLev : listLeftNav) {
            for (SecondLevel key : listScdLev) {
                listProduct = findProductBySecondId(key.getSecondId());
                map.put(key, listProduct);
            }
            list.add(map);
        }
        return list;
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

    // 方案状态列表
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

    // 删除客户方案信息（连同这个方案的房间、产品信息一并删除）
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

    //批量删除客户方案信息
    @RequestMapping(value = "customer/solutions/batchdelete", method = RequestMethod.POST)
    public ReturnResult deleteSolutions(@RequestBody List<Solutions> solutionsList) {
        for (Solutions solutions:solutionsList) {
            ReturnResult result = deleteSolutions(solutions.getSoluId());
            if (result.getStatus() == 0) {
                // 事务回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ReturnResult.returnResult(false, "其中一些方案关联了模板方案，不能删除");
            }
        }
        return ReturnResult.returnResult(true);
    }

    // 分页查询方案列表
    @RequestMapping(value = "customer/solutions")
    public IPage<Solutions> findSolutions(Solutions solutions, HttpServletRequest request, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        if ("设计人员".equals(CookieUtil.getCookieValue(request,"roleName","utf-8"))) {
            User design = new User();
            Integer designId = Integer.valueOf(CookieUtil.getCookieValue(request, "userId"));
            design.setId(designId);
            solutions.setDesign(design);
        }
        Page<Solutions> page = new Page<>(current,size);
        return solutionsService.findSolutionsList(page,solutions);
    }

    // 根据id查询单条报价数据
    @RequestMapping(value = "customer/quote")
    public Quote findQuote(Integer quoteId) {
        return quoteService.getById(quoteId);
    }

    // 根据id修改单条报价数据
    @RequestMapping(value = "customer/quote/update",method = RequestMethod.POST)
    public ReturnResult updateQuote(Quote quote) {
        boolean update = quoteService.update(quote, new UpdateWrapper<Quote>().eq("quote_id", quote.getQuoteId()));
        return ReturnResult.returnResult(update);
    }

    // 根据id删除方案内的单条报价数据
    @RequestMapping(value = "customer/quote/delete", method = RequestMethod.POST)
    public ReturnResult deleteQuote(Integer quoteId) {
        boolean removeById = quoteService.removeById(quoteId);
        return ReturnResult.returnResult(removeById);
    }

    // 批量删除报价单
    @RequestMapping(value = "customer/quote/batchdelete")
    public ReturnResult batchdeleteQuote(@RequestBody ArrayList<Integer> ids) {
        boolean removeByIds = quoteService.removeByIds(ids);
        return ReturnResult.returnResult(removeByIds);
    }

    /**
     * @description: 编辑施工费用
     */
    @RequestMapping(value = "customer/quote/editWorkPrice", method = RequestMethod.POST)
    public ReturnResult editWorkPrice(Solutions solutions) {
        boolean updateById = solutionsService.updateById(solutions);
        return ReturnResult.returnResult(updateById);
    }

    /**
     * @description: 编辑备注
     */
    @RequestMapping(value = "customer/quote/editSoluDesc", method = RequestMethod.POST)
    public ReturnResult editSoluDesc(Solutions solutions) {
        boolean updateById = solutionsService.updateById(solutions);
        return ReturnResult.returnResult(updateById);
    }

    /**
     * @description: 设计人员下载报价单
     * @param soluId : 方案id
     */
    @RequestMapping(value = "quote/toexcel")
    public void quoteToExcel(Integer soluId, HttpServletResponse response) throws IOException {
        List<Quote> quoteList = quoteService.findQuoteById(soluId);
        int i = 1;
        // 省级-市级-县级-客户 产品总价
        double provincePriceTotal = 0, cityPriceTotal = 0, countyPriceTotal = 0, totalPrice = 0;
        for (Quote quote1:quoteList) {
            // 设置序号
            quote1.setId(i);
            // 省级每行合计
            quote1.setProvinceTotalPrice(quote1.getProvincePrice() * quote1.getProductNum());
            // 市级每行合计
            quote1.setCityTotalPrice(quote1.getCityPrice() * quote1.getProductNum());
            // 县级每行合计
            quote1.setCountyTotalPrice(quote1.getCountyPrice() * quote1.getProductNum());
            // 客户每行合计
            quote1.setTotalPrice(quote1.getPrice() * quote1.getProductNum());
            provincePriceTotal += quote1.getProvincePrice();
            cityPriceTotal += quote1.getCityPrice();
            countyPriceTotal += quote1.getCountyPrice();
            totalPrice += quote1.getTotalPrice();
            i = i + 1;
        }

        QuoteVo quoteInfo = quoteService.findQuoteInfo(soluId);

        String templateFileName ="xlsx" + File.separator + "template2.xlsx";
        try {
            response.setContentType("application/vnd.ms-excel");
            // 设置头
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setCharacterEncoding("utf-8");

            // 防止中文乱码
            String fileName = URLEncoder.encode(quoteInfo.getCusName() + "的智能家居方案报价单(仅供内部查看)", "UTF-8").replaceAll("\\+", "%20");

            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(new ClassPathResource(templateFileName).getInputStream()).build();

            WriteSheet writeSheet = EasyExcel.writerSheet(0).build();
            WriteSheet writeSheet2 = EasyExcel.writerSheet(1).build();
            WriteSheet writeSheet3 = EasyExcel.writerSheet(2).build();
            WriteSheet writeSheet4 = EasyExcel.writerSheet(3).build();

            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();

            // 填充excel
            excelWriter.fill(quoteList, fillConfig, writeSheet);
            excelWriter.fill(quoteList, fillConfig, writeSheet2);
            excelWriter.fill(quoteList, fillConfig, writeSheet3);
            excelWriter.fill(quoteList, fillConfig, writeSheet4);

            Map<String, Object> map = new HashMap<>();
            // 日期
            map.put("date", quoteInfo.getAddTime());
            // 报价单号
            map.put("quoteNum", quoteInfo.getQuoteNum());
            // 设计人员姓名
            map.put("designName", quoteInfo.getDesignName());
            // 电话
            map.put("designMobile", quoteInfo.getDesignMobile());
            // 客户姓名
            map.put("cusName", quoteInfo.getCusName());
            // 电话
            map.put("cusMobile", quoteInfo.getCusMobile());
            // 描述
            map.put("descr", quoteInfo.getDescr());

            // 代理 产品总价
            map.put("provincePriceTotal", provincePriceTotal);
            map.put("cityPriceTotal", cityPriceTotal);
            map.put("countyPriceTotal", countyPriceTotal);
            // 客户 产品总价
            map.put("totalPrice", totalPrice);

            // 施工费用名称
            map.put("workPriceName", quoteInfo.getWorkPriceName());

            // 施工费用
            double provinceWorkPrice, cityWorkPrice, countyWorkPrice, workPrice;
            provinceWorkPrice = provincePriceTotal * 0.1;
            cityWorkPrice = cityPriceTotal * 0.1;
            countyWorkPrice = countyPriceTotal * 0.1;
            if (quoteInfo.getWorkPrice() == 0) {
                workPrice = totalPrice * 0.1;
            }else {
                workPrice = quoteInfo.getWorkPrice();
            }

            map.put("provinceWorkPrice", provinceWorkPrice);
            map.put("cityWorkPrice", cityWorkPrice);
            map.put("countyWorkPrice", countyWorkPrice);
            map.put("workPrice", workPrice);

            // 报价单总价
            map.put("provinceTotal", provinceWorkPrice + provincePriceTotal);
            map.put("cityTotal", cityWorkPrice + cityPriceTotal);
            map.put("countyTotal", countyWorkPrice + countyPriceTotal);
            map.put("total", workPrice + totalPrice);

            // 填充excel
            excelWriter.fill(map, writeSheet);
            excelWriter.fill(map, writeSheet2);
            excelWriter.fill(map, writeSheet3);
            excelWriter.fill(map, writeSheet4);

            excelWriter.finish();
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>();
            map.put("status", "0");
            map.put("msg", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }

    }

    /**
     * @description: 上传方案效果图
     * @param soluId:方案id
     * @param soluName:方案名称
     * @param file:上传的图片
     * @return ReturnResult
     */
    @RequestMapping(value = "customer/renderings/save", method = RequestMethod.POST)
    public ReturnResult saveRenderings(Integer soluId, String soluName, @RequestParam(value = "file",required = false) MultipartFile file) throws IOException {
        if (soluId == null) {
            return ReturnResult.returnResult(false, "需要选择一个方案");
        }
        String saveFileName,rendPath,rendDesc,rendName;
        boolean save;
        Renderings renderings = new Renderings();
        if (file.getSize() == 0) {
            return ReturnResult.returnResult(false, "没有上传图片");
        }
        saveFileName = FileUtil.saveFile(file,uploadPath + uploadPath2 +"/renderings/"+soluName, uploadPath);
        rendPath = fileHost + saveFileName;
        rendDesc = "这是" +soluName+"的效果图";
        rendName = Objects.requireNonNull(file.getOriginalFilename()).substring(0, file.getOriginalFilename().lastIndexOf("."));
        renderings.setRendName(rendName);
        renderings.setRendDesc(rendDesc);
        renderings.setRendPath(rendPath);
        renderings.setSoluId(soluId);
        save = renderingsService.save(renderings);
        if (!save) {
            // 事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnResult.returnResult(false, "上传失败，请稍后重试");
        }
        return ReturnResult.returnResult(true);
    }

    /**
     * @description: 删除效果图
     * @param rendId:图片id
     * @return ReturnResult
     */
    @RequestMapping(value = "customer/renderings/delete", method = RequestMethod.POST)
    public ReturnResult deleteRenderings(Integer rendId) {
        String path;
        Renderings renderings = renderingsService.getById(rendId);
        path = uploadPath + renderings.getRendPath().substring(fileHost.length());
        // 删除服务器的图片
        FileUtil.deleteFile(path);
        // 删除数据库中的信息
        boolean removeById = renderingsService.removeById(rendId);
        return ReturnResult.returnResult(removeById);
    }


}
