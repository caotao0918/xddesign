package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.entity.Product;
import cn.zmdxd.xddesign.entity.ProductProperty;
import cn.zmdxd.xddesign.entity.ProductVo;
import cn.zmdxd.xddesign.util.ReturnResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 曹涛
 * @date 2021/1/27 9:54
 * @description:
 */
public interface ProductService extends IService<Product> {
    Integer saveProduct(ProductVo productVo);

    boolean saveProductProperty(Integer productId, Integer valueId);

//    IPage<Product> findProducts(Page<Product> page,Integer current, Integer size, ProductVo productVo);
    IPage<Product> findProducts(Page<Product> page, ProductVo productVo);
    List<Product> findProducts();

    boolean removeProductProperty(Integer valueId);

    List<Product> findProductsBySecond(Integer secondId);

    Product findProduct(Integer productId);

    boolean removeProductPropertyByProductId(Integer productId);

    List<ProductProperty> findProductPropertyByProductId(Integer productId);
}
