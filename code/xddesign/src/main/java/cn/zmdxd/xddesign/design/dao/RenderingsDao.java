package cn.zmdxd.xddesign.design.dao;

import cn.zmdxd.xddesign.entity.Renderings;
import cn.zmdxd.xddesign.entity.Room;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author 曹涛
 * @date 2021/2/3 11:28
 * @description:
 */
public interface RenderingsDao extends BaseMapper<Renderings> {
    IPage<Renderings> selectRenderings(Page<Renderings> page, @Param("room") Room room);
}
