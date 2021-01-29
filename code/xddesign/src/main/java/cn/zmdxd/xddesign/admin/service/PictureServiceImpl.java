package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.admin.dao.PictureDao;
import cn.zmdxd.xddesign.entity.Picture;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/27 9:43
 * @description: 产品图片接口实现类
 */
@Service
public class PictureServiceImpl extends ServiceImpl<PictureDao, Picture> implements PictureService {

    @Autowired
    private PictureDao pictureDao;

    @Override
    public boolean savePicture(Picture picture, Integer productId) {
        Map<String,Object> map = new HashMap<>();
        map.put("productId",productId);
        map.put("picture",picture);
        return pictureDao.insertPicture(map) == 1;
    }

    @Override
    public Picture getPictureById(Integer id) {
        return pictureDao.getPictureById(id);
    }
}
