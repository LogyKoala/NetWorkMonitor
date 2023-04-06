package com.shian.monitor.utils

import java.util.LinkedList
import java.util.regex.Pattern

object PatternUtils {
    /**
     * 验证是否为手机号的正则
     */
    val phonePattern: Pattern =
        Pattern.compile("((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}")

    /**
     * 获取符合正则的字符串
     */
    fun patternGroup(pattern: Pattern, str: String): LinkedList<String> {
        val groupList = LinkedList<String>()
        try {
            // 将给定的正则表达式编译到模式中
            val matcher = pattern.matcher(str)
            while (matcher.find()) {
                val group = matcher.group()
                groupList.add(group)
            }
        } catch (_: Exception) {
        }
        return groupList
    }
}