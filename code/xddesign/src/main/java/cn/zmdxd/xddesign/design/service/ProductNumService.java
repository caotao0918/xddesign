package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.entity.ProductNum;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 曹涛
 * @date 2021/2/1 13:49
 * @description:
 */
public interface ProductNumService extends IService<ProductNum> {

    Integer saveProductNum(ProductNum productNum);

}
