package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.admin.dao.GuideDao;
import cn.zmdxd.xddesign.entity.Guide;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 曹涛
 * @date 2021/1/25 13:52
 * @description: 产品手册接口实现类
 */
@Service
public class GuideServiceImpl extends ServiceImpl<GuideDao, Guide> implements GuideService {
}
