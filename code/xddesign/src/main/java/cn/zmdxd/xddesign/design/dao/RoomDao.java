package cn.zmdxd.xddesign.design.dao;

import cn.zmdxd.xddesign.entity.Room;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/22 9:32
 * @description:
 */
public interface RoomDao extends BaseMapper<Room> {

    int insertRoom(Room room);

    IPage<Room> selectRoom(Page<Room> page, @Param("room")Room room);
}
