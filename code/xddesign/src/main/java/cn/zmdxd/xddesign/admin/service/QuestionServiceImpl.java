package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.admin.dao.QuestionDao;
import cn.zmdxd.xddesign.entity.Question;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 曹涛
 * @date 2021/1/25 15:07
 * @description: 产品常见问题接口实现类
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionDao, Question> implements QuestionService {
}
