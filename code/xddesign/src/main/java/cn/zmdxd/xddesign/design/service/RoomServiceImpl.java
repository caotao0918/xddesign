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
    public ReturnResult saveRoom(Integer soluId, Room room) {
        Map<String,Object> map = new HashMap<>();
        map.put("soluId",soluId);
        map.put("room",room);
        int insert = roomDao.insertRoom(map);
        ReturnResult result = new ReturnResult();
        if (insert!=1) {
            result.setStatus(0);
            result.setMsg("添加失败，请稍后重试");
        } else {
            result.setStatus(1);
            result.setMsg("添加成功");
        }
        return result;
    }
}
