package cn.zmdxd.xddesign.admin.dao;

import cn.zmdxd.xddesign.entity.Picture;
import cn.zmdxd.xddesign.entity.ProductVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/27 9:40
 * @description:
 */
public interface PictureDao extends BaseMapper<Picture> {

    Integer insertPicture(Map<String, Object> map);

    Picture getPictureById(Integer id);

    IPage<Picture> selectPicture(Page<Picture> page, @Param("productVo")ProductVo productVo);

}
