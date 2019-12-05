package com.esimtek.gemalto.util;

import java.util.regex.Pattern;

public class RegexUtil {

    /**
     * 正则：密码
     */
    private static final String REGEX_PASSWORD = "(?=.*[0-9])(?=.*[a-zA-Z])(?=([\\x21-\\x7e)]+)[^a-zA-Z0-9]).{8,30}";

    /**
     * 验证密码
     * <p>取值范围为a-z，A-Z，0-9，特殊字符，8-30位，必须包含数字和特殊字符，最少三种类型</p>
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isPassword(CharSequence input) {
        return isMatch(REGEX_PASSWORD, input);
    }

    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    private static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

}
