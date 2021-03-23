package cn.zmdxd.xddesign.admin.dao;

import cn.zmdxd.xddesign.entity.Guide;
import cn.zmdxd.xddesign.entity.ProductVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author 曹涛
 * @date 2021/1/25 13:51
 * @description:
 */
public interface GuideDao extends BaseMapper<Guide> {
    IPage<Guide> selectGuides(Page<Guide> page, @Param("productVo") ProductVo productVo);
}
