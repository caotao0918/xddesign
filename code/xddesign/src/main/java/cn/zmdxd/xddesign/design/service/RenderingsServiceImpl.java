package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.design.dao.RenderingsDao;
import cn.zmdxd.xddesign.entity.Renderings;
import cn.zmdxd.xddesign.entity.Room;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 曹涛
 * @date 2021/2/3 11:30
 * @description:
 */
@Service
public class RenderingsServiceImpl extends ServiceImpl<RenderingsDao, Renderings> implements RenderingsService {

    @Autowired
    private RenderingsDao renderingsDao;

    @Override
    public IPage<Renderings> findRenderings(Page<Renderings> page, Room room) {
        return renderingsDao.selectRenderings(page, room);
    }

    @Override
    public void copyRenderings(Integer soluId, Integer soluId1, String cusName) {
        String desc = "客户" + cusName + "的快速方案的效果图";
        renderingsDao.copyRenderings(soluId, soluId1, desc);
    }

}
