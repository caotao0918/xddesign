package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.entity.House;
import cn.zmdxd.xddesign.utils.ReturnResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 曹涛
 * @date 2021/1/21 10:29
 * @description:
 */
public interface HouseService extends IService<House> {
    ReturnResult saveHouse(House house);
}
