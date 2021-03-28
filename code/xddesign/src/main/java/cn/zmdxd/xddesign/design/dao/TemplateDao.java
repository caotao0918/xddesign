package cn.zmdxd.xddesign.design.dao;

import cn.zmdxd.xddesign.entity.Template;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/2/7 11:38
 * @description:
 */
public interface TemplateDao extends BaseMapper<Template> {

    IPage<Template> selectTemplatesPage(Page<Template> page);

    Template selectTemplate(Integer tempId);

    Integer selectTypeIdBySoluId(Integer soluId);

    int insertTemplate(Map<String, Object> map);

    Template selectTemplateBySoluId(Integer soluId);

    Template selectTemplateByHouseId(Integer houseId);

    List<Template> selectTemplateByTypeId(Integer typeId);
}
