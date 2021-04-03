package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.entity.Template;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/2/7 11:39
 * @description:
 */
public interface TemplateService extends IService<Template> {
    IPage<Template> findTemplateList(Page<Template> page, Template template);

    Template findTemplateById(Integer tempId);

    Integer findTypeIdBySoluId(Integer soluId);

    boolean saveTemplate(Template template, Integer typeId, Integer designId);

    Template findTemplateBySoluId(Integer soluId);

    Template findTemplateByHouseId(Integer houseId);

    List<Template> findTemplateByTypeId(Integer typeId);
}
