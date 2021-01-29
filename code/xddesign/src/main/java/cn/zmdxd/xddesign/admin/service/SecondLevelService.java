package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.entity.SecondLevel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/1/24 10:08
 * @description:
 */
public interface SecondLevelService extends IService<SecondLevel> {

    Boolean saveSecondLevel(Integer firstId, SecondLevel secondLevel);

    IPage<SecondLevel> findSecondLevels(Page<SecondLevel> page, Integer firstId);

    SecondLevel findSecondLevel(Integer secondId);
}
