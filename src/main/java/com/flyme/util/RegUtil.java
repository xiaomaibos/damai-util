package com.flyme.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则解析
 *
 * @author zzzz76
 */
public class RegUtil {

    /**
     * 按照reg解析text,获取第i组
     *
      * @param text
     * @param reg
     * @param i
     * @return
     */
    public static String regFind(String text, String reg, int i){
        Matcher m = Pattern.compile(reg, Pattern.CASE_INSENSITIVE).matcher(text);
        return m.find() ? m.group(i) : "";
    }

    /**
     * 按照reg解析text,获取第1组
     *
     * @param text
     * @param reg
     * @return
     */
    public static String regFind(String text, String reg){
        return regFind(text, reg, 1);
    }

    /**
     * 按照reg解析text,获取第i组(区分大小写)
     *
     * @param text
     * @param reg
     * @param i
     * @return
     */
    public static String regFindCaseSensitive(String text, String reg, int i){
        Matcher m = Pattern.compile(reg).matcher(text);
        return m.find() ? m.group(i) : "";
    }

    /**
     * 按照reg解析text,获取第1组(区分大小写)
     *
     * @param text
     * @param reg
     * @return
     */
    public static String regFindCaseSensitive(String text, String reg){
        return regFindCaseSensitive(text, reg, 1);
    }
}
