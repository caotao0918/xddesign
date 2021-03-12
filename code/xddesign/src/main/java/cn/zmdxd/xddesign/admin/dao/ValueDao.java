package cn.zmdxd.xddesign.admin.dao;

import cn.zmdxd.xddesign.entity.ProductProperty;
import cn.zmdxd.xddesign.entity.ProductVo;
import cn.zmdxd.xddesign.entity.PropertyValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @author 曹涛
 * @date 2021/1/27 9:46
 * @description:
 */
public interface ValueDao extends BaseMapper<PropertyValue> {

    Integer insertValue(PropertyValue propertyValue);
//    Integer insertValue(Map<String, Object> map);
    IPage<ProductVo> selectProductPropertyValueList(Page<ProductVo> page, @Param("productVo") ProductVo productVo);

    List<ProductProperty> selectByProductIdAndPropertyId(ProductVo productVo);
}
