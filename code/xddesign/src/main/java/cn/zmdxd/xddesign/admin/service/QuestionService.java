package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.entity.ProductVo;
import cn.zmdxd.xddesign.entity.Question;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 曹涛
 * @date 2021/1/25 15:05
 * @description:
 */
public interface QuestionService extends IService<Question> {
    IPage<Question> findQuestions(Page<Question> page, ProductVo productVo);
}
