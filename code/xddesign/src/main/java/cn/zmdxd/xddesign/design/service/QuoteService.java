package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.entity.Quote;
import cn.zmdxd.xddesign.entity.Room;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 曹涛
 * @date 2021/2/1 17:02
 * @description:
 */
public interface QuoteService extends IService<Quote> {
    IPage<Quote> findQuotes(Page<Quote> page, Room room);
}
