package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.entity.Product;
import cn.zmdxd.xddesign.entity.ProductVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 曹涛
 * @date 2021/1/27 9:54
 * @description:
 */
public interface ProductService extends IService<Product> {
    Integer saveProduct(ProductVo productVo);

    boolean saveProductProperty(Integer productId, Integer valueId);

    IPage<Product> findProducts(Page<Product> page, Integer productId);

    boolean removeProductProperty(Integer valueId);

    IPage<Product> findProductsBySecond(Page<Product> page, Integer secondId);
}
