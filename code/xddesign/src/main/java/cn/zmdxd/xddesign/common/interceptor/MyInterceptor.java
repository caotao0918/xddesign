package cn.zmdxd.xddesign.common.interceptor;

import cn.zmdxd.xddesign.util.CookieUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 曹涛
 * @date 2021/3/8 10:10
 * @description: 自定义拦截器 如果未登录，则跳转到产品中心(这里不跳转到登陆页面的原因是有两个登陆页面，不好做判断)
 */
@Configuration
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String contextPath = "/xddesign";
        String path = request.getServletPath();
        String userId = CookieUtil.getCookieValue(request, "userId");
        String cusId = CookieUtil.getCookieValue(request, "customerId");
        String roleName = CookieUtil.getCookieValue(request, "roleName", "utf-8");

        if (path.startsWith("/html/customer") || path.startsWith("/customer")) {
            if (cusId == null) {
                if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                    //告诉ajax我是重定向
                    response.setHeader("REDIRECT", "REDIRECT");
                    //告诉ajax我重定向的路径
                    response.setHeader("CONTENTPATH", "/xddesign/html/login/cuslogin.html");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    response.sendRedirect("/xddesign/html/login/cuslogin.html");
                }
                return false;
            }
        }else if (path.startsWith("/html/dsg")){
            if (userId == null || !"设计人员".equals(roleName)) {
                if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                    //告诉ajax我是重定向
                    response.setHeader("REDIRECT", "REDIRECT");
                    //告诉ajax我重定向的路径
                    response.setHeader("CONTENTPATH", "/xddesign/html/login/userlogin.html");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    response.sendRedirect("/xddesign/html/login/userlogin.html");
                }
                return false;
            }
        }else if (path.startsWith("/html/admin")){
            if (userId == null || !"管理员".equals(roleName)) {
                if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                    //告诉ajax我是重定向
                    response.setHeader("REDIRECT", "REDIRECT");
                    //告诉ajax我重定向的路径
                    response.setHeader("CONTENTPATH", "/xddesign/html/login/userlogin.html");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    response.sendRedirect("/xddesign/html/login/userlogin.html");
                }
                return false;
            }
        }else if (path.startsWith("/user")){
            if (userId == null) {
                if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                    //告诉ajax我是重定向
                    response.setHeader("REDIRECT", "REDIRECT");
                    //告诉ajax我重定向的路径
                    response.setHeader("CONTENTPATH", "/xddesign/html/login/userlogin.html");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    response.sendRedirect("/xddesign/html/login/userlogin.html");
                }
                return false;
            }
        }else {
        }
        return true;
    }
}
