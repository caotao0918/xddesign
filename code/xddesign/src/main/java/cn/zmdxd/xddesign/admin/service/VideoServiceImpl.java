package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.admin.dao.VideoDao;
import cn.zmdxd.xddesign.entity.ProductVo;
import cn.zmdxd.xddesign.entity.Video;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 曹涛
 * @date 2021/1/24 16:28
 * @description: 产品视频接口实现类
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoDao, Video> implements VideoService {

    @Autowired
    private VideoDao videoDao;

    @Override
    public IPage<Video> findVideos(Page<Video> page, ProductVo productVo) {
        return videoDao.selectVideos(page, productVo);
    }
}
