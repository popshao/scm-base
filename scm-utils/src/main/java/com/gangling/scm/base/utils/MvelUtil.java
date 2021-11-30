package com.gangling.scm.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.mvel2.MVEL;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MvelUtil {

    /*
     *  Example:
     *  String expression = "number = number + 2; return number;";
     *  Map<String, Object> vars = new HashMap<String, Object>();
     *  vars.put("number", 1);
     *
     *  return 3;
     */
    public static Object eval(String expression, Map<String, Object> vars) {
        Object eval = null;
        try {
            eval = MVEL.eval(expression, vars);
        } catch (Exception e) {
            log.error("", e);
        }
        return eval;
    }

    /**
     * 给对象的指定属性设置值
     *
     * @param object   要设值的对象
     * @param property 要设值的属性名
     * @param value    要设置的属性值
     */
    public static void setValue(Object object, String property, Object value) {
        String expression = "object." + property + "=value";
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("object", object);
        vars.put("value", value);
        MVEL.eval(expression, vars);
    }

    /**
     * 返回对象的指定属性的值
     *
     * @param object   要取值的对象
     * @param property 要取值的属性名
     */
    public static Object getValue(Object object, String property) {
        String expression = "return object." + property;
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("object", object);
        return MVEL.eval(expression, vars);
    }
}