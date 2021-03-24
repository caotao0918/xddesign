package cn.zmdxd.xddesign.admin.dao;

import cn.zmdxd.xddesign.entity.ProductVo;
import cn.zmdxd.xddesign.entity.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author 曹涛
 * @date 2021/1/24 16:27
 * @description:
 */
public interface VideoDao extends BaseMapper<Video> {
    IPage<Video> selectVideos(Page<Video> page, @Param("productVo") ProductVo productVo);
}
