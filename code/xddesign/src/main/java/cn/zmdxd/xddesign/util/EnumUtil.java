package cn.zmdxd.xddesign.util;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * java枚举类转List<Map>集合
     *
     * @param clazz
     * @return null-该class不是枚举类型  []-该枚举类型没有自定义字段  list-获取该枚举类型的List<Map>返回结果
     */
    public static List<Map<String, Object>> enumToListMap(Class<?> clazz) {
        List<Map<String, Object>> resultList = null;
        // 判断是否是枚举类型
        if ("java.lang.Enum".equals(clazz.getSuperclass().getCanonicalName())) {
            resultList = new ArrayList<>();
            // 获取所有public方法
            Method[] methods = clazz.getMethods();
            List<Field> fieldList = new ArrayList<>();
            for (int i = 0; i < methods.length; i++) {
                String methodName = methods[i].getName();
                if (methodName.startsWith("get") && !"getDeclaringClass".equals(methodName)
                        && !"getClass".equals(methodName)) { // 找到枚举类中的以get开头的(并且不是父类已定义的方法)所有方法
                    Field field = null;
                    try {
                        field = clazz.getDeclaredField(StringUtils.uncapitalize(methodName.substring(3))); // 通过方法名获取自定义字段
                    } catch (NoSuchFieldException | SecurityException e) {
                        e.printStackTrace();
                    }
                    if (field != null) { // 如果不为空则添加到fieldList集合中
                        fieldList.add(field);
                    }
                }
            }
            if (!fieldList.isEmpty()) { // 判断fieldList集合是否为空
                Map<String, Object> map = null;
                Enum<?>[] enums = (Enum[])clazz.getEnumConstants(); // 获取所有枚举
                for (int i = 0; i < enums.length; i++) {
                    map = new HashMap<>();
                    for (int l = 0, len = fieldList.size(); l < len; l++) {
                        Field field = fieldList.get(l);
                        field.setAccessible(true);
                        try {
                            map.put(field.getName(), field.get(enums[i])); // 向map集合添加字段名称 和 字段值
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    resultList.add(map);// 将Map添加到集合中
                }
            }
        }
        return resultList;
    }
}
