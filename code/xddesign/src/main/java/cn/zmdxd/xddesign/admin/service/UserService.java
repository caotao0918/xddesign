package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.entity.User;
import cn.zmdxd.xddesign.utils.ReturnResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 曹涛
 * @date 2021/1/17 16:26
 * @description:
 */
public interface UserService extends IService<User> {
    String saveOrUpdateUser(User user);

    Object findUser(Integer id, Integer current, Integer size);

    User findByMobile(String mobile);

}
