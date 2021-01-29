package cn.zmdxd.xddesign.admin.dao;

import cn.zmdxd.xddesign.entity.SecondLevel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/24 10:06
 * @description:
 */
public interface SecondLevelDao extends BaseMapper<SecondLevel> {


    int insertSecondLevel(Map<String, Object> map);

    IPage<SecondLevel> findSecondLevels(Page<SecondLevel> page, @Param("firstId") Integer firstId);

    SecondLevel findSecondLevel(Integer secondId);

}
