package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.admin.dao.PropertyDao;
import cn.zmdxd.xddesign.entity.Property;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    public Boolean saveProperty(Integer secondId, Property property) {
        Map<String,Object> map = new HashMap<>();
        map.put("secondId",secondId);
        map.put("property",property);
        int insertProperty = propertyDao.insertProperty(map);
        return insertProperty ==1;

    }
}
