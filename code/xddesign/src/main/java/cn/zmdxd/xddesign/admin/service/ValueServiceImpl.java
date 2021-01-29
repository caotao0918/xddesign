package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.admin.dao.ValueDao;
import cn.zmdxd.xddesign.entity.PropertyValue;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/27 9:50
 * @description: 产品属性值接口实现类
 */
@Service
public class ValueServiceImpl extends ServiceImpl<ValueDao, PropertyValue> implements ValueService {

    @Autowired
    private ValueDao valueDao;

    @Override
    public Integer saveValue(PropertyValue propertyValue) {
//        Map<String,Object> map = new HashMap<>();
//        map.put("valueName",propertyValue.getValueName());
//        map.put("propertyId",propertyValue.getProperty().getPropertyId());
        return valueDao.insertValue(propertyValue);
    }
}
