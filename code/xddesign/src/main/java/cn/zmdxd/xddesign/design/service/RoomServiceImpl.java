package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.design.dao.RoomDao;
import cn.zmdxd.xddesign.entity.Room;
import cn.zmdxd.xddesign.utils.ReturnResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/22 9:36
 * @description:
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomDao, Room> implements RoomService {

    @Autowired
    private RoomDao roomDao;

    @Override
    public Integer saveRoom(Room room) {
        return roomDao.insertRoom(room);
    }
}
