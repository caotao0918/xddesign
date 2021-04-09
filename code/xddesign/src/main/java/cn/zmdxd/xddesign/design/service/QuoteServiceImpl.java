package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.design.dao.QuoteDao;
import cn.zmdxd.xddesign.entity.Quote;
import cn.zmdxd.xddesign.entity.QuoteVo;
import cn.zmdxd.xddesign.entity.Room;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 曹涛
 * @date 2021/2/1 17:03
 * @description:
 */
@Service
public class QuoteServiceImpl extends ServiceImpl<QuoteDao, Quote> implements QuoteService {

    @Autowired
    private QuoteDao quoteDao;

    @Override
    public IPage<Quote> findQuotes(Page<Quote> page, Room room) {
        return quoteDao.selectQuotes(page, room);
    }

    @Override
    public QuoteVo findQuoteInfo(Integer soluId) {
        return quoteDao.selectQuoteInfo(soluId);
    }
}
