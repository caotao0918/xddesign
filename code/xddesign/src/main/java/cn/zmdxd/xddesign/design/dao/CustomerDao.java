package cn.zmdxd.xddesign.design.dao;

import cn.zmdxd.xddesign.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/19 9:14
 * @description:
 */
public interface CustomerDao extends BaseMapper<Customer> {
    int insertCustomer(Customer customer);

    int updateCustomer(Customer customer);

    IPage<Customer> selectCustomers(Page<?> page, @Param("designId") Integer designId);

    Customer selectCustomer(Integer id);
}
