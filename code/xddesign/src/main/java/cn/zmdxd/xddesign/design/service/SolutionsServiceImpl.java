package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.design.dao.SolutionsDao;
import cn.zmdxd.xddesign.entity.Solutions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/21 15:47
 * @description:
 */
@Service
public class SolutionsServiceImpl extends ServiceImpl<SolutionsDao, Solutions> implements SolutionsService {

    @Autowired
    private SolutionsDao solutionsDao;

    @Override
    public String saveSolutions(Integer houseId, Integer designId, Solutions solutions) {
        solutions.setState("设计中");
        Map<String, Object> map = new HashMap<>();
        map.put("houseId", houseId);
        map.put("designId", designId);
        map.put("solutions", solutions);
        int i = solutionsDao.insertSolutions(map);
        if (i != 1) return "添加失败";
        else return "添加成功";
    }

    @Override
    public IPage<Solutions> findSolutionsList(Page<Solutions> page, Integer designId) {
        return solutionsDao.selectSolutionsList(page,designId);
    }

}
