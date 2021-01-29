package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.admin.dao.SecondLevelDao;
import cn.zmdxd.xddesign.entity.SecondLevel;
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
 * @date 2021/1/24 10:15
 * @description: 产品二级分类接口实现类
 */
@Service
public class SecondLevelServiceImpl extends ServiceImpl<SecondLevelDao, SecondLevel> implements SecondLevelService {

    @Autowired
    private SecondLevelDao secondLevelDao;

    @Override
    public Boolean saveSecondLevel(Integer firstId, SecondLevel secondLevel) {
        Map<String, Object> map = new HashMap<>();
        map.put("firstId",firstId);
        map.put("secondLevel",secondLevel);
        int insertSecondLevel = secondLevelDao.insertSecondLevel(map);
        return insertSecondLevel == 1;
    }

    @Override
    public IPage<SecondLevel> findSecondLevels(Page<SecondLevel> page, Integer firstId) {
        return secondLevelDao.findSecondLevels(page, firstId);
    }

    @Override
    public SecondLevel findSecondLevel(Integer secondId) {
        return secondLevelDao.findSecondLevel(secondId);
    }
}
