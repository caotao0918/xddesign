package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.entity.ProductVo;
import cn.zmdxd.xddesign.entity.Video;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 曹涛
 * @date 2021/1/24 16:28
 * @description:
 */
public interface VideoService extends IService<Video> {

    IPage<Video> findVideos(Page<Video> page, ProductVo productVo);
}
