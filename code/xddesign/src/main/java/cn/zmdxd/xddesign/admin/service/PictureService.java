package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
