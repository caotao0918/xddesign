package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.entity.ProductProperty;
import cn.zmdxd.xddesign.entity.ProductVo;
import cn.zmdxd.xddesign.entity.PropertyValue;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/1/27 9:49
 * @description:
 */
public interface ValueService extends IService<PropertyValue> {

    Integer saveValue(PropertyValue propertyValue);

    IPage<ProductVo> findProductPropertyValueList(Page<ProductVo> page, ProductVo productVo);

    List<ProductProperty> findByProductIdAndPropertyId(ProductVo productVo);
}
