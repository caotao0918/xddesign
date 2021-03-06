package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.entity.Quote;
import cn.zmdxd.xddesign.entity.QuoteVo;
import cn.zmdxd.xddesign.entity.Room;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/2/1 17:02
 * @description:
 */
public interface QuoteService extends IService<Quote> {
    IPage<Quote> findQuotes(Page<Quote> page, Room room);

    QuoteVo findQuoteInfo(Integer soluId);

    List<Quote> findQuoteById(Integer soluId);

}
