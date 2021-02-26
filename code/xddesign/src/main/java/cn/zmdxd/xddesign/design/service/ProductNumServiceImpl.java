package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.design.dao.ProductNumDao;
import cn.zmdxd.xddesign.entity.ProductNum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 曹涛
 * @date 2021/2/1 13:51
 * @description:
 */
@Service
public class ProductNumServiceImpl extends ServiceImpl<ProductNumDao, ProductNum> implements ProductNumService {

    @Autowired
    private ProductNumDao productNumDao;

    @Override
    public Integer saveProductNum(ProductNum productNum) {
        return productNumDao.insertProductNum(productNum);
    }

    @Override
    public IPage<ProductNum> findProductNum(Page<ProductNum> page, ProductNum productNum) {
        return productNumDao.selectProductNum(page,productNum);
    }

}
