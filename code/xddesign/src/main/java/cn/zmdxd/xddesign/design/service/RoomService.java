package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.entity.Room;
import cn.zmdxd.xddesign.utils.ReturnResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 曹涛
 * @date 2021/1/22 9:34
 * @description:
 */
public interface RoomService extends IService<Room>  {
    ReturnResult saveRoom(Integer soluId, Room room);
}
