package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.entity.Property;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/26 10:58
 * @description:
 */
public interface PropertyService extends IService<Property> {

    Boolean saveProperty(Integer secondId, Property property);

}
