package cn.zmdxd.xddesign.design.dao;

import cn.zmdxd.xddesign.entity.Room;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/22 9:32
 * @description:
 */
public interface RoomDao extends BaseMapper<Room> {


    int insertRoom(Room room);
}
