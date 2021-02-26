package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.admin.dao.PropertyDao;
import cn.zmdxd.xddesign.entity.Property;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/26 10:58
 * @description: 产品属性接口实现类
 */
@Service
public class PropertyServiceImpl extends ServiceImpl<PropertyDao, Property> implements PropertyService {

    @Autowired
    private PropertyDao propertyDao;

    @Override
    public Boolean saveProperty(Property property) {
        int insertProperty = propertyDao.insertProperty(property);
        return insertProperty ==1;

    }

    @Override
    public IPage<Property> findPropertyList(Page<Property> page, Property property) {
        return propertyDao.selectProperty(page, property);
    }
}
