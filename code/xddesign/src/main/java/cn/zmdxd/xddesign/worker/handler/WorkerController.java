package cn.zmdxd.xddesign.worker.handler;

import cn.zmdxd.xddesign.design.service.SolutionsService;
import cn.zmdxd.xddesign.entity.Solutions;
import cn.zmdxd.xddesign.entity.TaskVO;
import cn.zmdxd.xddesign.util.ReturnResult;
import cn.zmdxd.xddesign.worker.service.TaskService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 曹涛
 * @date 2021/4/12 13:41
 * @description: 施工人员
 */
@RestController
@RequestMapping(value = "/worker/", produces = {"application/json;charset=UTF-8;" })
@Transactional
public class WorkerController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private SolutionsService solutionsService;

    /**
     * @description: 获取未施工的订单
     */
    @RequestMapping(value = "order/unfinished")
    public List<TaskVO> getUnfinishedTask() {
        return taskService.getselectUnfinishedTask();
    }
    /**
     * @description: 获取施工完毕的订单
     */
    @RequestMapping(value = "order/finished")
    public List<TaskVO> getFinishedTask() {
        return taskService.getselectFinishedTask();
    }

    /**
     * @description: 施工完毕
     */
    @RequestMapping(value = "over", method = RequestMethod.POST)
    public ReturnResult overWork(Integer soluId, String workName) {
        Timestamp workTime = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        boolean update = solutionsService.update(new UpdateWrapper<Solutions>()
                .eq("solu_id", soluId).set("work_name", workName).set("work_time", workTime).set("state", "施工完毕"));
        return ReturnResult.returnResult(update);
    }

}
