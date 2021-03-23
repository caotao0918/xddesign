package cn.zmdxd.xddesign.admin.dao;

import cn.zmdxd.xddesign.entity.Product;
import cn.zmdxd.xddesign.entity.ProductProperty;
import cn.zmdxd.xddesign.entity.ProductVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/27 9:53
 * @description:
 */
public interface ProductDao extends BaseMapper<Product> {

    Integer insertProductProerty(@Param("productId") Integer productId, @Param("valueId") Integer valueId);

    Integer insertProduct(ProductVo productVo);

//    IPage<Product> findProducts(Page<Product> page, @Param("offset") Integer offset, @Param("size")Integer size, @Param("productVo") ProductVo productVo);
    IPage<Product> findProducts(Page<Product> page, @Param("productVo") ProductVo productVo);
    List<Product> findProductList();
    Product findProduct(Integer productId);

    int removeProductProperty(Integer valueId);

    int countProduct(ProductVo productVo);

    List<Product> findProductsBySecond(Integer secondId);

    int deleteProductPropertyByProductId(Integer productId);

    List<ProductProperty> selectProductPropertyByProductId(Integer productId);
}
