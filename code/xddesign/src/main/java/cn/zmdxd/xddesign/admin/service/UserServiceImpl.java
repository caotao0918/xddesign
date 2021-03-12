package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.admin.dao.UserDao;
import cn.zmdxd.xddesign.design.dao.CustomerDao;
import cn.zmdxd.xddesign.entity.Customer;
import cn.zmdxd.xddesign.entity.User;
import cn.zmdxd.xddesign.util.MD5Utils;
import cn.zmdxd.xddesign.util.PhoneUtil;
import cn.zmdxd.xddesign.util.ReturnResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private CustomerDao customerDao;

    //管理员添加系统用户
    @Override
    public String saveOrUpdateUser(User user) {

        ReturnResult result = new ReturnResult();
        Map<String,Object> map = new HashMap<>();
        map.put("roleId",user.getRole().getId());
        if (!PhoneUtil.isMobile(user.getMobile()) && !PhoneUtil.isPhone(user.getMobile()))
            return "手机号格式错误";
        Integer integer;
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
           *  可供修改项有：用户名、手机号、密码、角色
           * */
            if (user.getPwd() != null && !user.getPwd().trim().equals("")) {
                if (user.getPwd().trim().length() < 6 || user.getPwd().trim().length() > 20) return "密码长度在6-20位之间";
                user.setPwd(MD5Utils.generate(user.getPwd()));
            }else {
                user.setPwd(null);
            }
            List<Customer> customerList = customerDao.selectList(new QueryWrapper<Customer>().eq("design_id", user.getId()));
            if (customerList.size()!=0) {
                if (!user.getRole().getId().equals(findRoleIdByUserId(user.getId()))) {
                    return "不能修改该用户角色";
                }
            }

            integer = userDao.update(user, new UpdateWrapper<User>().eq("id", user.getId()).set("role_id",user.getRole().getId()));
        }
        if (integer != 1)
            return "操作失败，请稍后重试";
        return "操作成功";
    }

    @Override
    public Object findUser(User user, Integer current, Integer size) {
        Page<User> page = new Page<>(current, size);
        IPage<User> iPage = userDao.findUser(page, user);
        if (user.getId() != null) {
            return iPage.getRecords().get(0);
        }
        return iPage;
    }

    @Override
    public User findByMobile(String mobile) {
        return userDao.findByMobile(mobile);
    }

    @Override
    public List<User> findDesignList() {
        return userDao.selectDesignList();
    }

    @Override
    public Integer findRoleIdByUserId(Integer id) {
        return userDao.selectRoleIdUserByUserId(id);
    }

}
