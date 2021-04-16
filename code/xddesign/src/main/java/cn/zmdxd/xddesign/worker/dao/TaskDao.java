package cn.zmdxd.xddesign.worker.dao;

import cn.zmdxd.xddesign.entity.Solutions;
import cn.zmdxd.xddesign.entity.TaskVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/4/12 14:54
 * @description:
 */
public interface TaskDao extends BaseMapper<Solutions> {

    List<TaskVO> selectUnfinishedTask();
    List<TaskVO> selectFinishedTask();

}
