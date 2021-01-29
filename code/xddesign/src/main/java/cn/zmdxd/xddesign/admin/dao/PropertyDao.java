package cn.zmdxd.xddesign.admin.dao;

import cn.zmdxd.xddesign.entity.Property;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/26 10:52
 * @description:
 */
public interface PropertyDao extends BaseMapper<Property> {

    int insertProperty(Map<String,Object> map);

}
