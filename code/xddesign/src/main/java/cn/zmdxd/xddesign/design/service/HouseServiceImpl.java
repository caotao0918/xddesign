package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.design.dao.HouseDao;
import cn.zmdxd.xddesign.entity.House;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/21 10:30
 * @description:
 */
@Service
public class HouseServiceImpl extends ServiceImpl<HouseDao, House> implements HouseService {

    @Autowired
    private HouseDao houseDao;

    @Override
    public String saveHouse(Integer customerId, House house) {
        Map<String,Object> map = new HashMap<>();
        map.put("customerId",customerId);
        map.put("house",house);
        int i = houseDao.insertHouse(map);
        if (i!=1) return "添加失败，请稍后重试";
        return "添加成功";
    }
}
