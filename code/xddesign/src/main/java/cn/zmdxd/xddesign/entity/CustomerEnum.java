package cn.zmdxd.xddesign.entity;

import cn.zmdxd.xddesign.utils.BaseEnum;
import lombok.Getter;

@Getter
public enum CustomerEnum implements BaseEnum {

    PERSONAL(0,"个人"),
    DISTRICT(1,"小区"),
    RENOVATION(2,"装修队"),
    DEVELOPER(3,"开发商"),
    OTHER(4,"其他");

    private final Integer code;
    private final String msg;

    CustomerEnum(Integer code,String msg) {
        this.code=code;
        this.msg=msg;
    }

}
