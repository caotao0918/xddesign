package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.design.dao.CustomerDao;
import cn.zmdxd.xddesign.entity.Customer;
import cn.zmdxd.xddesign.entity.CustomerEnum;
import cn.zmdxd.xddesign.util.EnumUtil;
import cn.zmdxd.xddesign.util.MD5Utils;
import cn.zmdxd.xddesign.util.PhoneUtil;
import cn.zmdxd.xddesign.util.ReturnResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 曹涛
 * @date 2021/1/19 9:52
 * @description:
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerDao, Customer> implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public ReturnResult saveOrUpdateCustomer(Customer customer) {
        ReturnResult result = new ReturnResult();

        String desc = EnumUtil.getMsgByCode(CustomerEnum.class, customer.getCode());
        customer.setDesc(desc);

        if (!PhoneUtil.isMobile(customer.getMobile()) && !PhoneUtil.isPhone(customer.getMobile())) {
            result.setMsg("电话或手机号码格式错误");
            result.setStatus(0);
            return result;
        }

        if (customer.getId() == null) {
            //新增客户
            if (customerDao.selectOne(new QueryWrapper<Customer>().eq("mobile",customer.getMobile()))!=null) {
                result.setMsg("该客户已存在，无需重复添加");
                result.setStatus(0);
                return result;
            }

            customer.setPwd(MD5Utils.generate("123456"));
            int insertCustomer = customerDao.insertCustomer(customer);
            return ReturnResult.returnResult(insertCustomer == 1);
        }else {
            //修改客户信息,可供修改的项有：客户名称、客户电话、客户类别、客户登陆密码、客户需求、客户地址; 管理员还可以修改该客户所属的设计人员
            if (customer.getPwd() != null && !customer.getPwd().trim().equals("")) {
                if (customer.getPwd().length()<6 || customer.getPwd().length()>20) {
//                    result.setMsg("密码长度在6-20位之间");
//                    result.setStatus(0);
//                    return result;
                    return ReturnResult.returnResult(false,"密码长度在6-20位之间");
                }
                customer.setPwd(MD5Utils.generate(customer.getPwd()));
            }else {
                customer.setPwd(null);
            }
            int update = customerDao.update(customer, new UpdateWrapper<Customer>().eq("id", customer.getId()).set("design_id", customer.getDesign().getId()));
            return ReturnResult.returnResult(update == 1);
        }
    }

    @Override
    public IPage<Customer> findCustomers(Page<Customer> page, Customer customer) {
        return customerDao.selectCustomers(page, customer);
    }

    @Override
    public Customer findCustomer(Integer id) {
        return customerDao.selectCustomer(id);
    }
}
