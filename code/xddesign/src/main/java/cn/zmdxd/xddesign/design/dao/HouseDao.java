package cn.zmdxd.xddesign.design.dao;

import cn.zmdxd.xddesign.entity.Customer;
import cn.zmdxd.xddesign.entity.House;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/21 10:27
 * @description:
 */
public interface HouseDao extends BaseMapper<House> {
    int insertHouse(House house);

    IPage<House> selectHouse(Page<House> page, @Param("house") House house);
}
