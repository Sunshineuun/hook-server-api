package com.eju.hookserver.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author qiushengming
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 判断当前字符是不是汉字
     *
     * @param c 字符
     * @return 是&否
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;

    }

    public static String encode(String url, String enc) {
        StringBuilder sb = new StringBuilder();
        char[] urlChar = url.toCharArray();
        for (char c : urlChar) {
            try {
                if (isChinese(c)) {
                    sb.append(URLEncoder.encode(String.valueOf(c), enc));
                } else {
                    sb.append(c);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    /**
     * 异常堆栈信息转String
     *
     * @param e 异常
     * @return String
     */
    public static String stackTraceInfoToStr(Exception e) {
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append(e.toString())
                .append("      ")
                .append(e.getMessage());
        // 限制长度20；将其转换为String；并通过 \n 连接起来
        String stackTraceInfo = Arrays.stream(e.getStackTrace())
                .limit(20)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
        errorMsg.append(stackTraceInfo);
        return errorMsg.toString();
    }
}
