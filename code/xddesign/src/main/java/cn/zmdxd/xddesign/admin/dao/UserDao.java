package cn.zmdxd.xddesign.admin.dao;

import cn.zmdxd.xddesign.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/17 16:37
 * @description:
 */
public interface UserDao extends BaseMapper<User> {

    Integer insertUser(Map<String,Object> map);

    Integer updateUser(Map<String, Object> map);

    IPage<User> findUser(Page<?> page, @Param("user") User user);

    User findByMobile(String mobile);

    List<User> selectDesignList();

}
