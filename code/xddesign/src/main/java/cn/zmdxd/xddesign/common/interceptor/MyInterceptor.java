package cn.zmdxd.xddesign.common.interceptor;

import cn.zmdxd.xddesign.util.CookieUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 曹涛
 * @date 2021/3/8 10:10
 * @description:
 */
@Configuration
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = CookieUtil.getCookieValue(request, "userId");
        String cusId = CookieUtil.getCookieValue(request, "customerId");
        if (userId == null && cusId == null){
            if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
                //告诉ajax我是重定向
                response.setHeader("REDIRECT", "REDIRECT");
                //告诉ajax我重定向的路径
                response.setHeader("CONTENTPATH", "/customer/goodslist.html");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }else{
                response.sendRedirect("/customer/goodslist.html");
            }
            return false;
        }else {
            return true;
        }
    }
}
