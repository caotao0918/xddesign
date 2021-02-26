package cn.zmdxd.xddesign.login.handler;

import cn.zmdxd.xddesign.admin.service.UserService;
import cn.zmdxd.xddesign.design.service.CustomerService;
import cn.zmdxd.xddesign.entity.Customer;
import cn.zmdxd.xddesign.entity.User;
import cn.zmdxd.xddesign.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 曹涛
 * @date 2021/1/17 16:06
 * @description: 系统用户和客户登录模块
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;

    //验证码生成
    @RequestMapping("/login/imgcode.jpg")
    public void getCode3(HttpServletResponse response, HttpSession session) throws IOException {
        int width = 100,height = 30,codeCount=4,lineCount=10;
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 生成验证码对象
        CaptchaUtil code = new CaptchaUtil(width, height, codeCount, lineCount);
        session.setAttribute("imgcode",code.getCode());
        //设置过期时间为1分钟
        session.setMaxInactiveInterval(60);
        code.write(response.getOutputStream());
    }

    //系统用户登录
    @RequestMapping(value = "/user/login",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8;"})
    @ResponseBody
    public EntityResult<User> login(String mobile, String password, String imgcode, HttpSession session, Model model, HttpServletRequest request,HttpServletResponse response) {

        EntityResult<User> userResult = new EntityResult<>();

        User user = userService.findByMobile(mobile);
        String msg;//登录错误信息
        int status = 0;   //登陆状态 0代表失败/1代表成功
        //要跳转到的页面
//        String htmlPage = "登陆页面";

//        if (!imgcode.equals(session.getAttribute("imgcode")) || session.getAttribute("imgcode")==null) {
//            msg = "验证码错误或已过期";
//            userResult.setMsg(msg);
//            userResult.setStatus(status);
//            return userResult;
//        }
        if (user == null) {
            msg = "用户不存在";
            userResult.setMsg(msg);
            userResult.setStatus(status);
            return userResult;
        } else {
            if (!MD5Utils.verify(password, user.getPwd())) {
                msg = "密码错误";
                userResult.setMsg(msg);
                userResult.setStatus(status);
                return userResult;
            }
            msg = "登陆成功";
            status = 1;

            //保存用户id、角色保存到cookie中
            CookieUtil.setCookie(request,response,"userId",user.getId().toString(),60*60*24*30);
            CookieUtil.setCookie(request,response,"roleName",user.getRole().getName(),60*60*24*30,"UTF-8");

            //更新上次登陆时间
            String lastTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            userService.update(new UpdateWrapper<User>().eq("id",user.getId()).set("last_time", lastTime));
        }
        userResult.setData(user);
        userResult.setMsg(msg);
        userResult.setStatus(status);
        return userResult;
    }

    //系统用户改密
    @RequestMapping(value = "/user/modifypassword", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8;"})
    @ResponseBody
    public ReturnResult modifyUserPassword(String oldPassword, String password, HttpServletRequest request) {
        Integer userId = Integer.valueOf(CookieUtil.getCookieValue(request, "userId"));
        User user = userService.getOne(new QueryWrapper<User>().eq("id",userId).select("pwd"));
        if (!MD5Utils.verify(oldPassword, user.getPwd())) return ReturnResult.returnResult(false);
        password = MD5Utils.generate(password);
        boolean update = userService.update(new UpdateWrapper<User>().eq("id", userId).set("pwd", password));
        return ReturnResult.returnResult(update);
    }

    //用户退出系统
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8;"})
    @ResponseBody
    public ReturnResult userLogout(HttpServletRequest request,HttpServletResponse response) {
        ReturnResult result = new ReturnResult();
        if (CookieUtil.removeCookie(request,response,"userId") && CookieUtil.removeCookie(request,response,"roleName")) {
            result.setStatus(1);
            result.setMsg("退出成功");
            return result;
        }
        result.setStatus(0);
        result.setMsg("退出失败,请稍后重试");
        return result;
    }

    //客户登录
    @RequestMapping(value = "/customer/login", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8;"})
    @ResponseBody
    public EntityResult<Customer> customerLogin(String mobile, String pwd, HttpServletRequest request,HttpServletResponse response) {

        EntityResult<Customer> customerResult = new EntityResult<>();

        Customer customer = customerService.getOne(new QueryWrapper<Customer>().eq("mobile",mobile),false);

//        if (!imgcode.equals(session.getAttribute("imgcode")) || session.getAttribute("imgcode")==null) {
//            customerResult.setStatus(0);
//            customerResult.setMsg("验证码错误或已过期");
//            return customerResult;
//        }
        if (customer == null) {
            customerResult.setStatus(0);
            customerResult.setMsg("您输入的账号不存在,请确认无误后再次输入");
            return customerResult;
        } else {
            if (!MD5Utils.verify(pwd, customer.getPwd())) {
                customerResult.setStatus(0);
                customerResult.setMsg("密码错误");
                return customerResult;
            }

            //保存用户id到cookie中
            CookieUtil.setCookie(request,response,"customerId",customer.getId().toString(),60*60*24*30);

        }

        customerResult.setData(customer);
        customerResult.setMsg("登陆成功");
        customerResult.setStatus(1);
        return customerResult;

    }

    //客户改密
    @RequestMapping(value = "/customer/modifypassword", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8;"})
    @ResponseBody
    public ReturnResult modifyCustomerPassword(String oldPassword, String password,HttpServletRequest request) {
        Integer customerId = Integer.valueOf(CookieUtil.getCookieValue(request, "customerId"));
        Customer customer = customerService.getOne(new QueryWrapper<Customer>().eq("id", customerId).select("pwd"));
        if (!MD5Utils.verify(oldPassword,customer.getPwd())) return ReturnResult.returnResult(false);
        password = MD5Utils.generate(password);
        boolean update = customerService.update(new UpdateWrapper<Customer>().eq("id", customerId).set("pwd", password));
        return ReturnResult.returnResult(update);
    }

    //客户退出系统
    @RequestMapping(value = "/customer/logout", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8;"})
    @ResponseBody
    public ReturnResult customerLogout(HttpServletRequest request,HttpServletResponse response) {
        boolean removeCookie = CookieUtil.removeCookie(request, response, "customerId");
        return ReturnResult.returnResult(removeCookie);
    }

}
