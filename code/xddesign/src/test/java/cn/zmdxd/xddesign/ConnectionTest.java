package cn.zmdxd.xddesign;

import cn.zmdxd.xddesign.admin.service.PictureService;
import cn.zmdxd.xddesign.admin.service.UserService;
import cn.zmdxd.xddesign.entity.CustomerEnum;
import cn.zmdxd.xddesign.entity.Picture;
import cn.zmdxd.xddesign.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author 曹涛
 * @date 2021/1/17 14:51
 * @description:
 */
public class ConnectionTest {

    @Autowired
    private UserService userService;
    @Autowired
    private PictureService pictureService;

    @Test
    public void test1() {
        CustomerEnum[] values = CustomerEnum.values();
        System.out.println(Arrays.toString(values));
    }

    @Test
    public void updateById() {
        User user = new User();
        user.setId(18);
        user.setUsername("caotao");
        userService.updateById(user);
    }

    public boolean ttt(boolean t){
        return t;
    }

    @Test
    public void booleanOrTrue(){
        boolean state1 = ttt(false);
        boolean ttt = ttt(true);
        if (state1 & ttt) System.out.println(111);
        else System.out.println(222);
    }

}
