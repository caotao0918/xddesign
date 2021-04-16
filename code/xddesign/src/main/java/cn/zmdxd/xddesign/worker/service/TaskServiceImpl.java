package cn.zmdxd.xddesign.worker.service;

import cn.zmdxd.xddesign.entity.Solutions;
import cn.zmdxd.xddesign.entity.TaskVO;
import cn.zmdxd.xddesign.worker.dao.TaskDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/4/12 15:22
 * @description:
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskDao, Solutions> implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Override
    public List<TaskVO> getselectUnfinishedTask() {
        return taskDao.selectUnfinishedTask();
    }

    @Override
    public List<TaskVO> getselectFinishedTask() {
        return taskDao.selectFinishedTask();
    }
}
