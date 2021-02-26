package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.entity.Customer;
import cn.zmdxd.xddesign.util.ReturnResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 曹涛
 * @date 2021/1/19 9:51
 * @description:
 */
public interface CustomerService extends IService<Customer> {
    ReturnResult saveOrUpdateCustomer(Customer customer);

    IPage<Customer> findCustomers(Page<Customer> page, Customer customer);

    Customer findCustomer(Integer id);
}
