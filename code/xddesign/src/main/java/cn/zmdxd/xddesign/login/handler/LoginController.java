package cn.zmdxd.xddesign.login.handler;

import cn.zmdxd.xddesign.admin.service.UserService;
import cn.zmdxd.xddesign.design.service.CustomerService;
import cn.zmdxd.xddesign.entity.Customer;
import cn.zmdxd.xddesign.entity.User;
import cn.zmdxd.xddesign.utils.CaptchaUtil;
import cn.zmdxd.xddesign.utils.CookieUtil;
import cn.zmdxd.xddesign.utils.MD5Utils;
import cn.zmdxd.xddesign.utils.EntityResult;
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
    public String login(String mobile, String password, String imgcode, HttpSession session, Model model, HttpServletRequest request,HttpServletResponse response) {

        EntityResult<User> userResult = new EntityResult<>();

        User user = userService.findByMobile(mobile);
        String msg;//登录错误信息
        int status = 0;   //登陆状态 0代表失败/1代表成功
        //要跳转到的页面
        String htmlPage = "登陆页面";

//        System.out.println(MD5Utils.verify(password,user.getPwd()));

        if (!imgcode.equals(session.getAttribute("imgcode")) || session.getAttribute("imgcode")==null) {
            msg = "验证码错误或已过期";
            return msg;
        }
        if (user == null) {
            msg = "用户不存在";
            return msg;
        } else {
            if (!MD5Utils.verify(password, user.getPwd())) {
                msg = "密码错误";
                return msg;
            }
            msg = "登陆成功";
            status = 1;

            //保存用户id、角色保存到cookie中
            CookieUtil.setCookie(request,response,"userId",user.getId().toString(),60*60*24*30);
            CookieUtil.setCookie(request,response,"roleName",user.getRole().getName(),60*60*24*30,"UTF-8");

            //更新上次登陆时间
            String lastTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            userService.update(new UpdateWrapper<User>().eq("id",user.getId()).set("last_time", lastTime));

            //根据用户角色跳转到不同页面
            switch (user.getRole().getName()) {
                case "管理员":
                    htmlPage = "管理员页面";
                    break;
                case "设计人员":
                    htmlPage = "设计人员页面";
                    break;
                case "施工人员":
                    htmlPage = "施工人员页面";
                    break;
                case "仓管":
                    htmlPage = "仓管页面";
                    break;
                default:
                    htmlPage = "登陆页面";

            }
        }
//        userResult.setData(user);
//        userResult.setMsg(msg);
//        userResult.setStatus(status);
//        model.addAttribute("userResult",userResult);
        return htmlPage;
    }

    //系统用户改密
    @RequestMapping(value = "/user/modifypassword", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8;"})
    @ResponseBody
    public String modifyUserPassword(String password,HttpServletRequest request) {
        password = MD5Utils.generate(password);
        boolean update = userService.update(new UpdateWrapper<User>().eq("id", Integer.valueOf(CookieUtil.getCookieValue(request, "userId"))).set("pwd", password));
        if (!update) return "操作失败，请稍后重试";
        return "修改成功";
    }

    //用户退出系统
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8;"})
    @ResponseBody
    public String userLogout(HttpServletRequest request,HttpServletResponse response) {
        if (CookieUtil.removeCookie(request,response,"userId") && CookieUtil.removeCookie(request,response,"roleName")) return "退出成功";
        else return "退出失败,请稍后重试";
    }

    //客户登录
    @RequestMapping(value = "/customer/login", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8;"})
    @ResponseBody
    public String customerLogin(String mobile, String password, String imgcode, HttpSession session, Model model, HttpServletRequest request,HttpServletResponse response) {

        EntityResult<Customer> customerResult = new EntityResult<>();

        Customer customer = customerService.getOne(new QueryWrapper<Customer>().eq("mobile",mobile),false);
        System.out.println(customer);
        String msg;//登录错误信息
        int status = 0;   //登陆状态 0代表失败/1代表成功

        if (!imgcode.equals(session.getAttribute("imgcode")) || session.getAttribute("imgcode")==null) {
            msg = "验证码错误或已过期";
            return msg;
        }
        if (customer == null) {
            msg = "您输入的账号不存在,请确认无误后再次输入";
            return msg;
        } else {
            if (!MD5Utils.verify(password, customer.getPwd())) {
                msg = "密码错误";
                return msg;
            }
            msg = "登陆成功";
            status = 1;

            //保存用户id、角色保存到cookie中
            CookieUtil.setCookie(request,response,"customerId",customer.getId().toString(),60*60*24*30);

        }
//        customerResult.setData(customer);
//        customerResult.setMsg(msg);
//        customerResult.setStatus(status);
//        model.addAttribute("customerResult",customerResult);
        return "客户主页面";

    }

    //客户改密
    @RequestMapping(value = "/customer/modifypassword", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8;"})
    @ResponseBody
    public String modifyCustomerPassword(String password,HttpServletRequest request) {
        password = MD5Utils.generate(password);
        boolean update = customerService.update(new UpdateWrapper<Customer>().eq("id", Integer.valueOf(CookieUtil.getCookieValue(request, "customerId"))).set("pwd", password));
        if (!update) return "操作失败，请稍后重试";
        return "修改成功";
    }

    //客户退出系统
    @RequestMapping(value = "/customer/logout", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8;"})
    @ResponseBody
    public String customerLogout(HttpServletRequest request,HttpServletResponse response) {
        if (CookieUtil.removeCookie(request,response,"customerId")) return "退出成功";
        else return "退出失败,请稍后重试";
    }

}
