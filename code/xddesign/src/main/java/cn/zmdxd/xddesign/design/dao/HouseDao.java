package cn.zmdxd.xddesign.design.dao;

import cn.zmdxd.xddesign.entity.House;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/21 10:27
 * @description:
 */
public interface HouseDao extends BaseMapper<House> {
    int insertHouse(Map<String, Object> map);
}
