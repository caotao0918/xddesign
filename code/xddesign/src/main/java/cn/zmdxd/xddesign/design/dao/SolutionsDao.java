package cn.zmdxd.xddesign.design.dao;

import cn.zmdxd.xddesign.entity.Solutions;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/1/21 15:45
 * @description:
 */
public interface SolutionsDao extends BaseMapper<Solutions> {
    int insertSolutions(Solutions solutions);

    IPage<Solutions> selectSolutionsList(Page<?> page, @Param("solutions") Solutions solutions);

    Solutions selectSolutions(Integer soluId);

    List<Solutions> selectSolutionsByHouseId(Integer houseId);

}
