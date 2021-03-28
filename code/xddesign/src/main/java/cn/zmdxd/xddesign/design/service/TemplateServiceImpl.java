package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.design.dao.TemplateDao;
import cn.zmdxd.xddesign.entity.Template;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/2/7 11:40
 * @description:
 */
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateDao, Template> implements TemplateService{

    @Autowired
    private TemplateDao templateDao;

    @Override
    public IPage<Template> findTemplateList(Page<Template> page) {
        return templateDao.selectTemplatesPage(page);
    }

    @Override
    public Template findTemplateById(Integer tempId) {
        return templateDao.selectTemplate(tempId);
    }

    @Override
    public Integer findTypeIdBySoluId(Integer soluId) {
        return templateDao.selectTypeIdBySoluId(soluId);
    }

    @Override
    public boolean saveTemplate(Template template, Integer typeId, Integer designId) {
        Map<String,Object> map = new HashMap<>();
        map.put("template",template);
        map.put("typeId",typeId);
        map.put("designId",designId);
        int insertTemplate = templateDao.insertTemplate(map);
        return insertTemplate == 1;
    }

    @Override
    public Template findTemplateBySoluId(Integer soluId) {
        return templateDao.selectTemplateBySoluId(soluId);
    }

    @Override
    public Template findTemplateByHouseId(Integer houseId) {
        return templateDao.selectTemplateByHouseId(houseId);
    }

    @Override
    public List<Template> findTemplateByTypeId(Integer typeId) {
        return templateDao.selectTemplateByTypeId(typeId);
    }
}
