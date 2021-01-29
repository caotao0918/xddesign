package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.entity.PropertyValue;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 曹涛
 * @date 2021/1/27 9:49
 * @description:
 */
public interface ValueService extends IService<PropertyValue> {

    Integer saveValue(PropertyValue propertyValue);
}
