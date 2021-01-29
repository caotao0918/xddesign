package cn.zmdxd.xddesign.utils;

/**
 * @author 曹涛
 * @date 2021/1/19 13:59
 * @description:
 */
public class EnumUtil {

    public static <T extends BaseEnum> String getMsgByCode(Class<T> enumClass, Integer code) {
        for (T each : enumClass.getEnumConstants()) {
            if (each.getCode().equals(code)) {
                return each.getMsg();
            }
        }
        return "其他";
    }
}
