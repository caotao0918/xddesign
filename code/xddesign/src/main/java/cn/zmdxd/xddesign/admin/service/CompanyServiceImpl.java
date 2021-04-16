package cn.zmdxd.xddesign.admin.service;

import cn.zmdxd.xddesign.admin.dao.CompanyDao;
import cn.zmdxd.xddesign.entity.Company;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 曹涛
 * @date 2021/4/13 15:44
 * @description:
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyDao, Company> implements CompanyService {
}
