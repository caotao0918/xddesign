package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.design.dao.QuoteDao;
import cn.zmdxd.xddesign.entity.Quote;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 曹涛
 * @date 2021/2/1 17:03
 * @description:
 */
@Service
public class QuoteServiceImpl extends ServiceImpl<QuoteDao, Quote> implements QuoteService {
}
