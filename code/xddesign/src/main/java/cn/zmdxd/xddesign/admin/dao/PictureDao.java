package cn.zmdxd.xddesign.admin.dao;

import cn.zmdxd.xddesign.entity.Picture;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/27 9:40
 * @description:
 */
public interface PictureDao extends BaseMapper<Picture> {

    Integer insertPicture(Map<String, Object> map);

    Picture getPictureById(Integer id);

}
