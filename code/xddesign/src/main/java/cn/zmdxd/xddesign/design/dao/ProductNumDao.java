package cn.zmdxd.xddesign.design.dao;

import cn.zmdxd.xddesign.entity.ProductNum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author 曹涛
 * @date 2021/2/1 13:42
 * @description:
 */
public interface ProductNumDao extends BaseMapper<ProductNum> {

    int insertProductNum(ProductNum productNum);

    IPage<ProductNum> selectProductNum(Page<ProductNum> page, @Param("pn") ProductNum productNum);

}
