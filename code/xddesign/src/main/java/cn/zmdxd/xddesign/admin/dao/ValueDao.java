package cn.zmdxd.xddesign.admin.dao;

import cn.zmdxd.xddesign.entity.PropertyValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/27 9:46
 * @description:
 */
public interface ValueDao extends BaseMapper<PropertyValue> {

    Integer insertValue(PropertyValue propertyValue);
//    Integer insertValue(Map<String, Object> map);
}
