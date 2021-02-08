package cn.zmdxd.xddesign.entity;

import cn.zmdxd.xddesign.utils.BaseEnum;
import lombok.Getter;

@Getter
public enum SolutionsStateEnum implements BaseEnum {

    DESIGNING(0, "设计中"),
    DESIGNDONE(1,"设计完毕"),
    WORKING(2,"施工中"),
    WORKDONE(3,"施工完毕");

    private final Integer code;
    private final String msg;

    SolutionsStateEnum(Integer code,String msg) {
        this.code=code;
        this.msg=msg;
    }

}
