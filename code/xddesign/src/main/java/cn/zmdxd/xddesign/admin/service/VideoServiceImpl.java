package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.admin.dao.VideoDao;
import cn.zmdxd.xddesign.entity.Video;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 曹涛
 * @date 2021/1/24 16:28
 * @description: 产品视频接口实现类
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoDao, Video> implements VideoService {
}
