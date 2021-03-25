package cn.zmdxd.xddesign.design.dao;

import cn.zmdxd.xddesign.entity.Quote;
import cn.zmdxd.xddesign.entity.Room;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author 曹涛
 * @date 2021/2/1 17:01
 * @description:
 */
public interface QuoteDao extends BaseMapper<Quote> {
    IPage<Quote> selectQuotes(Page<Quote> page, @Param("room") Room room);
}
