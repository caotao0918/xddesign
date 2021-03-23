package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.entity.Picture;
import cn.zmdxd.xddesign.entity.ProductVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * @author 曹涛
 * @date 2021/1/27 9:42
 * @description:
 */
public interface PictureService extends IService<Picture> {

    boolean savePicture(Picture picture, Integer productId);

    /**
     * @description: 根据图片id查询图片信息
     * @param id:图片id
     * @return Picture
     */
    Picture getPictureById(Integer id);

    IPage<Picture> findPicture(Page<Picture> page, ProductVo productVo);
}
