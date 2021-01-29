package cn.zmdxd.xddesign.design.handler;

import cn.zmdxd.xddesign.design.service.CustomerService;
import cn.zmdxd.xddesign.design.service.HouseService;
import cn.zmdxd.xddesign.design.service.RoomService;
import cn.zmdxd.xddesign.design.service.SolutionsService;
import cn.zmdxd.xddesign.entity.*;
import cn.zmdxd.xddesign.utils.CookieUtil;
import cn.zmdxd.xddesign.utils.ReturnResult;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 曹涛
 * @date 2021/1/19 9:51
 * @description: 设计人员模块
 */
@RestController
@RequestMapping(value = "/design/", produces = { "application/json;charset=UTF-8;" })
public class DesignController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private HouseService houseService;
    @Autowired
    private SolutionsService solutionsService;
    @Autowired
    private RoomService roomService;

    /**
     * @description: 添加或修改客户信息，customer中id若为null，则添加客户信息，添加时区分是管理员添加还是设计人员添加；否则修改客户信息
     * @param customer:客户信息(客户名称、客户电话、客户需求、客户地址、客户类别)
     */
    @RequestMapping(value = "customer/saveOrUpdate", method = RequestMethod.POST)
    public String saveOrUpdateCustomer(Customer customer, HttpServletRequest request) {
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
    * @param designId:管理员查询时designId为null,故查询所有客户列表;
    * @param current:当前页
    * @param size:每页条数
    * @return IPage:分页对象
    */
    @RequestMapping("customers")
    public IPage<Customer> findCustomers(Integer designId, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "1") Integer size) {
        Page<Customer> page = new Page<>(current,size);
        return customerService.findCustomers(page, designId);
    }

    //根据id查询客户信息
    @RequestMapping("customer")
    public Customer findCustomer(Integer id) {
        return customerService.findCustomer(id);
    }

    //删除/批量删除客户
    @RequestMapping(value = "customer/delete",method = RequestMethod.POST)
    public String deleteCustomer(@RequestBody List<Integer> idList) {
        boolean removeByIds = customerService.removeByIds(idList);
        if (removeByIds)
            return "删除成功";
        return "删除失败，请稍后重试！";
    }

    /**
     * @description: 添加或修改客户房子信息，当house中的id为null时添加/不为null时修改
     * @param customerId:客户id
     * @param house:房子信息(房子名称、房子地址)
     */
    @RequestMapping(value = "customer/house/saveorupdate")
    public String saveOrUpdateHouse(Integer customerId, House house) {
        if (house.getHouseId() == null) {
            //新增房子信息
            return houseService.saveHouse(customerId,house);

        }else {
            //修改房子信息 可供修改的项有：房子名称、房子地址、房子户型
            boolean update = houseService.update(new UpdateWrapper<House>().eq("house_id", house.getHouseId()).set("house_name", house.getHouseName()).set("house_address", house.getHouseAddress()).set("house_type",house.getHouseType()).set("house_reserve1",house.getHouseReserve1()).set("house_reserve2",house.getHouseReserve2()).set("house_reserve3",house.getHouseReserve3()));
            if (update) return "修改成功";
            else return "修改失败";

        }
    }

    //删除客户房子信息（连同这个房子的方案、房间、产品一并删除）
    @RequestMapping(value = "customer/house/delete",method = RequestMethod.POST)
    public String deleteHouse(Integer houseId) {
        boolean removeById = houseService.removeById(houseId);
        if (removeById)
            return "删除成功";
        else
            return "删除失败";
    }

    /**
     * @description: 添加或修改方案信息，当solutions中的id为null时添加/不为null时修改
     * @param houseId:客户房子id
     * @param solutions:方案信息(方案名称、方案描述、方案状态)
     */
    @RequestMapping(value = "customer/solutions/saveorupdate")
    public String saveOrUpdateSolutions(Integer houseId, Solutions solutions, HttpServletRequest request) {
        if (solutions.getSoluId() == null) {
            //新增方案信息
            Integer designId = Integer.valueOf(CookieUtil.getCookieValue(request,"userId"));
            return solutionsService.saveSolutions(houseId, designId, solutions);

        } else {
            //修改方案信息 可供修改的项有：方案名称、方案状态、方案描述
            boolean update = solutionsService.update(new UpdateWrapper<Solutions>().eq("solu_id", solutions.getSoluId()).set("solu_name",solutions.getSoluName()).set("state", solutions.getState()).set("solu_desc", solutions.getSoluDesc()).set("solu_reserve1",solutions.getSoluReserve1()).set("solu_reserve2",solutions.getSoluReserve2()).set("solu_reserve3",solutions.getSoluReserve3()));
            if (update) return "修改成功";
            else return "修改失败";

        }
    }

    //删除客户方案信息（连同这个方案的房间、产品信息一并删除）
    @RequestMapping(value = "customer/solutions/delete",method = RequestMethod.POST)
    public String deleteSolutions(Integer soluId) {
        boolean removeById = solutionsService.removeById(soluId);
        if (removeById)
            return "删除成功";
        else
            return "删除失败";
    }

    //分页查询设计人员设计的方案列表(不论客户)
    @RequestMapping(value = "solutions")
    public IPage<Solutions> findSolutions(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "1") Integer size) {
        //从cookie中找到设计人员id
        Integer designId = Integer.valueOf(CookieUtil.getCookieValue(request, "userId"));
        Page<Solutions> page = new Page<>(current,size);
        return solutionsService.findSolutionsList(page,designId);
    }

    //根据方案id查询方案详情




    /**
     * @description: 添加或修改房间信息，当room中的id为null时添加/不为null时修改
     * @param soluId:方案id
     * @param room:房间信息(房间名称、房间描述)
     **/
    @RequestMapping(value = "customer/room/saveorupdate",method = RequestMethod.POST)
    public ReturnResult saveOrUpdate(Integer soluId, Room room) {
        if (room.getRoomId() == null) {
            //添加房间
            return roomService.saveRoom(soluId,room);

        } else {
            //修改房间信息
            boolean update = roomService.update(new UpdateWrapper<Room>().eq("room_id",room.getRoomId()).set("room_name",room.getRoomName()).set("room_desc",room.getRoomDesc()).set("room_reserve1",room.getRoomReserve1()).set("room_reserve2",room.getRoomReserve2()).set("room_reserve3",room.getRoomReserve3()));
            ReturnResult result = new ReturnResult();
            if (!update) {
                result.setStatus(0);
                result.setMsg("修改失败，请稍后重试");

            } else {
                result.setStatus(1);
                result.setMsg("修改成功");
            }
            return result;

        }
    }




}
