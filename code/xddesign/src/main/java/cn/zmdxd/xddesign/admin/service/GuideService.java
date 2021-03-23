package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.entity.Guide;
import cn.zmdxd.xddesign.entity.ProductVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 曹涛
 * @date 2021/1/25 13:52
 * @description:
 */
public interface GuideService extends IService<Guide> {
    IPage<Guide> findGuides(Page<Guide> page, ProductVo productVo);
}
