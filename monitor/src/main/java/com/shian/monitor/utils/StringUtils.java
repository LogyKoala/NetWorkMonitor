package com.shian.monitor.utils;

import java.util.LinkedList;

public class StringUtils {

    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * [身份证号]
     * 显示前六位和后四位： 123456********0011
     * @param zjhm
     * @return
     * */
    public static String getDesensitization(String zjhm) {
        if (StringUtils.isEmpty(zjhm)) {
            return "";
        }
        int encLength = zjhm.length() - 10;
        String regex = "(\\d{6})\\d{" + encLength + "}(\\w{4})";//显示前六位和后四位
        // $1表示匹配第一个{}的内容
        return zjhm.replaceAll(regex, "$1********$2");
    }
    //另一种写法

    /**
     * [身份证号]
     * 前六位，后四位，其他用星号隐藏每位1个星号
     * 123456*********0011
     *
     * @param zjhm
     * @return
     */
    public static String idCard(String zjhm) {
        if (StringUtils.isEmpty(zjhm)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(zjhm.subSequence(0, 6));
        for (int i = 0; i < zjhm.length() - 10; i++) {
            sb.append("*");
        }
        sb.append(zjhm.substring(zjhm.length() - 4));
        return sb.toString();
    }


    /**
     * [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。
     * *************5762
     *
     * @param zjhm
     * @return
     */
    public static String idCardNum(String zjhm) {
        if (StringUtils.isEmpty(zjhm)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < zjhm.length() - 4; i++) {
            sb.append("*");
        }
        sb.append(zjhm.substring(zjhm.length() - 4));
        return sb.toString();
    }


    /**
     * [中文姓名] 如果长度为2，后一位隐藏为星号<例子：张*>，如果长度>2，中间隐藏为星号<例子：张*锤>
     *
     * @param fullName 姓名
     * @return
     */
    public static String chineseName(String fullName) {
        if (StringUtils.isEmpty(fullName)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(fullName.subSequence(0, 1));
        for (int i = 0; i < fullName.length() - 2; i++) {
            sb.append("*");
        }
        if (fullName.length() > 2) {
            sb.append(fullName.substring(fullName.length() - 1));
        } else {
            sb.append("*");
        }
        return sb.toString();
    }

    /**
     * [固定电话] 显示后四位，其他隐藏
     * 如 : ****1234
     *
     * @param num
     * @return
     */
    public static String fixedPhone(String num) {
        if (StringUtils.isEmpty(num)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num.length() - 4; i++) {
            sb.append("*");
        }
        sb.append(num.substring(num.length() - 4));
        return sb.toString();
    }

    /**
     * [手机号码] 前三位，后两位，其他隐藏<例子:138********34>
     *
     * @param num
     * @return
     */
    public static String mobilePhone(String num) {
        if (StringUtils.isEmpty(num)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(num.subSequence(0, 3));
        for (int i = 0; i < num.length() - 5; i++) {
            sb.append("*");
        }
        sb.append(num.substring(num.length() - 2));
        return sb.toString();
    }

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     *
     * @param address
     * @param sensitiveSize 敏感信息长度
     * @return
     */
    public static String address(String address, int sensitiveSize) {
        if (StringUtils.isEmpty(address)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < address.length() - sensitiveSize; i++) {
            sb.append("*");
        }
        sb.append(address.substring(address.length() - sensitiveSize));
        return sb.toString();
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     *
     * @param email
     * @return
     */
    public static String email(String email) {
        if (StringUtils.isEmpty(email)) {
            return "";
        }
        int index = email.indexOf("@");
        String end = email.substring(index);
        StringBuilder sb = new StringBuilder();
        sb.append(email.subSequence(0, 1));
        for (int i = 0; i < email.length() - end.length() - 1; i++) {
            sb.append("*");
        }
        sb.append(end);
        return sb.toString();

    }

    /**
     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234>
     *
     * @param cardNum
     * @return
     */
    public static String bankCard(String cardNum) {
        if (StringUtils.isEmpty(cardNum)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(cardNum.subSequence(0, 6));
        for (int i = 0; i < cardNum.length() - 10; i++) {
            sb.append("*");
        }
        sb.append(cardNum.substring(cardNum.length() - 4));
        return sb.toString();
    }

    /**
     * [公司开户银行联号] 公司开户银行联行号,显示前两位，其他用星号隐藏，每位1个星号<例子:12********>
     *
     * @param code
     * @return
     */
    public static String cnapsCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(code.subSequence(0, 2));
        for (int i = 0; i < code.length() - 2; i++) {
            sb.append("*");
        }
        return sb.toString();
    }

}

