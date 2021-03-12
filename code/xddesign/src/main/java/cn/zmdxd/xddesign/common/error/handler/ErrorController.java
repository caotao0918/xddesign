package cn.zmdxd.xddesign.common.error.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 曹涛
 * @date 2021/3/8 13:14
 * @description: 自定义404页面
 */
@Controller
@RequestMapping(value = "/error")
public class ErrorController {

    private static final String BASE_DIR = "/error/";


    @RequestMapping("/404")
    public String handle(HttpServletRequest request){
        return BASE_DIR + "error404.html";
    }


}
