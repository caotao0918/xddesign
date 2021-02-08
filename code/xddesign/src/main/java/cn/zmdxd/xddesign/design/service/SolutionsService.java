package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.entity.Solutions;
import cn.zmdxd.xddesign.utils.ReturnResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 曹涛
 * @date 2021/1/21 15:46
 * @description:
 */
public interface SolutionsService extends IService<Solutions> {

//    Integer saveSolutions(Solutions solutions);

    IPage<Solutions> findSolutionsList(Page<Solutions> page,  Integer designId);

    Solutions findSolutions(Integer soluId);

    List<Solutions> findSolutionsByHouseId(Integer houseId);

    ReturnResult saveOrUpdateSolution(Solutions solutions, HttpServletRequest request);
}
