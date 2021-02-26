package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.entity.Room;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 曹涛
 * @date 2021/1/22 9:34
 * @description:
 */
public interface RoomService extends IService<Room>  {
    Integer saveRoom(Room room);

    IPage<Room> findRoom(Page<Room> page, Room room);
}
