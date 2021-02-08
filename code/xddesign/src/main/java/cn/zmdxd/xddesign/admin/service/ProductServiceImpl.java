package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.admin.dao.ProductDao;
import cn.zmdxd.xddesign.entity.Product;
import cn.zmdxd.xddesign.entity.ProductVo;
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
 * @date 2021/1/27 9:55
 * @description: 产品信息接口实现类
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductDao, Product> implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Integer saveProduct(ProductVo productVo) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("secondId", product.getSecondLevel().getSecondId());
//        map.put("guideId", product.getGuide().getGuideId());
//        map.put("videoId", product.getVideo().getVideoId());
//        map.put("product", product);
        return productDao.insertProduct(productVo);
    }

    @Override
    public boolean saveProductProperty(Integer productId, Integer valueId) {
        return productDao.insertProductProerty(productId,valueId) == 1;
    }

    @Override
    public IPage<Product> findProducts(Page<Product> page,Integer productId, Integer current, Integer size) {
        Integer offset = (current-1)*size;
        return productDao.findProducts(page, productId, offset, size);
    }

    @Override
    public List<Product> findProducts() {
        return productDao.findProductList();
    }

    @Override
    public boolean removeProductProperty(Integer valueId) {
        int delete = productDao.removeProductProperty(valueId);
        return delete == 1;
    }

    @Override
    public IPage<Product> findProductsBySecond(Page<Product> page, Integer secondId,Integer current, Integer size) {
        Integer offset = (current-1)*size;
        return productDao.findProductsBySecond(page,secondId, offset, size);
    }
}
