package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.entity.Renderings;
import cn.zmdxd.xddesign.entity.Room;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 曹涛
 * @date 2021/2/3 11:29
 * @description:
 */
public interface RenderingsService extends IService<Renderings> {
    IPage<Renderings> findRenderings(Page<Renderings> page, Room room);

    void copyRenderings(Integer soluId, Integer soluId1, String cusName);
}
