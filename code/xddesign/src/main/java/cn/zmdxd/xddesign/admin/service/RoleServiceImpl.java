package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.admin.dao.RoleDao;
import cn.zmdxd.xddesign.entity.Role;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 曹涛
 * @date 2021/1/18 9:44
 * @description: 角色的增删改查操作接口实现类
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {
}
