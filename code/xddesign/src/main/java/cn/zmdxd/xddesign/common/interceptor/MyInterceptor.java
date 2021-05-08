package cn.zmdxd.xddesign.common.interceptor;

import cn.zmdxd.xddesign.util.CookieUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 曹涛
 * @date 2021/3/8 10:10
 * @description: 自定义拦截器
 */
@Configuration
public class MyInterceptor implements HandlerInterceptor {

    private static final String adminRoleName = "管理员";
    private static final String designRoleName = "设计人员";
    private static final String workRoleName = "施工人员";
    private static final String cusLoginPage = "/xddesign/html/login/cuslgi.html";
    private static final String userLoginPage = "/xddesign/html/login/userlgi.html";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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
                    response.setHeader("CONTENTPATH", cusLoginPage);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    response.sendRedirect(cusLoginPage);
                }
                return false;
            }
        }else if (path.startsWith("/html/dsg")){
            if (userId == null || !designRoleName.equals(roleName)) {
                if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                    //告诉ajax我是重定向
                    response.setHeader("REDIRECT", "REDIRECT");
                    //告诉ajax我重定向的路径
                    response.setHeader("CONTENTPATH", userLoginPage);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    response.sendRedirect(userLoginPage);
                }
                return false;
            }
        }else if (path.startsWith("/html/admin")){
            if (userId == null || !adminRoleName.equals(roleName)) {
                if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                    //告诉ajax我是重定向
                    response.setHeader("REDIRECT", "REDIRECT");
                    //告诉ajax我重定向的路径
                    response.setHeader("CONTENTPATH", userLoginPage);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    response.sendRedirect(userLoginPage);
                }
                return false;
            }
        }else if (path.startsWith("/html/worker")){
            if (userId == null || !workRoleName.equals(roleName)) {
                if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                    //告诉ajax我是重定向
                    response.setHeader("REDIRECT", "REDIRECT");
                    //告诉ajax我重定向的路径
                    response.setHeader("CONTENTPATH", userLoginPage);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    response.sendRedirect(userLoginPage);
                }
                return false;
            }
        }else if (path.startsWith("/user")){
            if (userId == null) {
                if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                    //告诉ajax我是重定向
                    response.setHeader("REDIRECT", "REDIRECT");
                    //告诉ajax我重定向的路径
                    response.setHeader("CONTENTPATH", userLoginPage);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    response.sendRedirect(userLoginPage);
                }
                return false;
            }
        }
        return true;
    }
}
