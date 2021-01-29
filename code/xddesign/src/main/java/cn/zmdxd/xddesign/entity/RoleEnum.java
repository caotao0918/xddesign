package cn.zmdxd.xddesign.entity;

import lombok.Getter;
import lombok.Setter;

//角色码枚举类
@Getter
public enum RoleEnum {
    ADMIN(0,"管理员"),
    DESIGNER(1,"设计人员"),
    WORKS(2,"施工人员"),
    WAREHOUSE(3,"仓管");

    private final int code;
    private final String msg;

    RoleEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
