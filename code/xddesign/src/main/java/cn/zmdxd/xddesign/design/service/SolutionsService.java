package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.entity.Solutions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/1/21 15:46
 * @description:
 */
public interface SolutionsService extends IService<Solutions> {
    String saveSolutions(Integer houseId, Integer designId, Solutions solutions);

    IPage<Solutions> findSolutionsList(Page<Solutions> page,  Integer designId);
}
