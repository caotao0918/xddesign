package cn.zmdxd.xddesign.admin.dao;

import cn.zmdxd.xddesign.entity.ProductVo;
import cn.zmdxd.xddesign.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author 曹涛
 * @date 2021/1/25 15:04
 * @description:
 */
public interface QuestionDao extends BaseMapper<Question> {
    IPage<Question> selectQuestions(Page<Question> page, @Param("productVo") ProductVo productVo);
}
