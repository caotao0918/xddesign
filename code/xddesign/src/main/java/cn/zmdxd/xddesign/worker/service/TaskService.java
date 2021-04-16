package cn.zmdxd.xddesign.worker.service;

import cn.zmdxd.xddesign.entity.Solutions;
import cn.zmdxd.xddesign.entity.TaskVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/4/12 15:18
 * @description:
 */
public interface TaskService extends IService<Solutions> {

    List<TaskVO> getselectUnfinishedTask();
    List<TaskVO> getselectFinishedTask();

}
