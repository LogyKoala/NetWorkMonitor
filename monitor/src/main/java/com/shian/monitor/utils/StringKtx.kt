package com.shian.monitor.utils

import java.util.LinkedList

/**
 * 对字符串进行加密
 * @param patternGroup 需要加密的字符串
 * @param block 加密规则
 */
fun String.encryptionStr(
    patternGroup: LinkedList<String>, block: (str: String) -> String
): String {
    if (patternGroup.isEmpty()) return this
    var newStr: String = this
    for (s in patternGroup) {
        newStr = newStr.replace(s, block.invoke(s))
    }
    return newStr
}
