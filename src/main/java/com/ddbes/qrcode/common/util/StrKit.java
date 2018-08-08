package com.ddbes.qrcode.common.util;

import java.util.Random;

/**
 * Created by daitian on 2018/6/4.
 */
public class StrKit {

    /**
     * 获取纯数字验证码
     *
     * @return
     */
    public static String getCode() {
        Random rd = new Random();
        StringBuffer sb = new StringBuffer();
        int getNum;
        do {
            getNum = Math.abs(rd.nextInt()) % 10 + 48;// 产生数字0-9的随机数
            // getNum = Math.abs(rd.nextInt())%26 + 97;//产生字母a-z的随机数
            sb.append(getNum);
        } while (sb.length() < 6);
        return sb.toString();
    }

    /**
     * 字符串为 null 或者内部字符全部为 ' ' '\t' '\n' '\r' 这四类字符时返回 true
     */
    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        int len = str.length();
        if (len == 0) {
            return true;
        }
        for (int i = 0; i < len; i++) {
            switch (str.charAt(i)) {
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    // case '\b':
                    // case '\f':
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    public static String append(Object ...o){
        StringBuffer sb = new StringBuffer();
        for (Object o1 : o) {
            if(o1!=null){
                sb.append(o1.toString());
            }
        }
        return sb.toString();
    }

    public static boolean equals(String a, String b) {
        return a == null ? b == null : a.equals(b);
    }

    public static String join(String[] stringArray, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<stringArray.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(stringArray[i]);
        }
        return sb.toString();
    }

    public static String join(String[] stringArray) {
        StringBuilder sb = new StringBuilder();
        for (String s : stringArray) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static String toCamelCase(String stringWithUnderline) {
        if (stringWithUnderline.indexOf('_') == -1) {
            return stringWithUnderline;
        }

        stringWithUnderline = stringWithUnderline.toLowerCase();
        char[] fromArray = stringWithUnderline.toCharArray();
        char[] toArray = new char[fromArray.length];
        int j = 0;
        for (int i=0; i<fromArray.length; i++) {
            if (fromArray[i] == '_') {
                // 当前字符为下划线时，将指针后移一位，将紧随下划线后面一个字符转成大写并存放
                i++;
                if (i < fromArray.length) {
                    toArray[j++] = Character.toUpperCase(fromArray[i]);
                }
            }
            else {
                toArray[j++] = fromArray[i];
            }
        }
        return new String(toArray, 0, j);
    }
}
