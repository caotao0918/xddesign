package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.admin.dao.UserDao;
import cn.zmdxd.xddesign.entity.User;
import cn.zmdxd.xddesign.utils.MD5Utils;
import cn.zmdxd.xddesign.utils.PhoneUtil;
import cn.zmdxd.xddesign.utils.ReturnResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/17 16:34
 * @description: 用户的增删改查操作接口实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private UserDao userDao;

    //管理员添加系统用户
    @Override
    public String saveOrUpdateUser(User user) {

        ReturnResult result = new ReturnResult();
        Map<String,Object> map = new HashMap<>();
        map.put("roleId",user.getRole().getId());
        if (!PhoneUtil.isMobile(user.getMobile()) && !PhoneUtil.isPhone(user.getMobile()))
            return "手机号格式错误";
        Integer integer = null;
        if (user.getId() == null) {
            //新增用户

            if (userDao.findByMobile(user.getMobile())!=null) return "该用户已存在，无需重复添加";

            Timestamp addTime = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            user.setAddTime(addTime);
            user.setPwd(MD5Utils.generate("123456"));
            map.put("user",user);
            integer = userDao.insertUser(map);

        }else {
           /* 修改用户信息
           *  可供修改项有：用户名、手机号、密码、角色、是否逻辑删除 五项
           * */
            if (user.getPwd() == null) return "密码不能为空";
            if (user.getPwd().length()<6 || user.getPwd().length()>20)
                return "密码长度在6-20位之间";
            user.setPwd(MD5Utils.generate(user.getPwd()));
            map.put("user",user);
            integer = userDao.updateUser(map);
        }
        if (integer != 1)
            return "操作失败，请稍后重试";
        return "操作成功";
    }

    @Override
    public Object findUser(Integer id, Integer current, Integer size) {
        Page<User> page = new Page<>(current, size);
        IPage<User> iPage = userDao.findUser(page,id);
        if (id!=null) {
            return iPage.getRecords().get(0);
        }
        return iPage;
    }

    @Override
    public User findByMobile(String mobile) {
        return userDao.findByMobile(mobile);
    }

}
