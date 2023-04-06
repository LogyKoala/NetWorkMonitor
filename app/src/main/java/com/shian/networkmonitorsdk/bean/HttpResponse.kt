package com.shian.networkmonitorsdk.bean

/**
 * 网络请求base bean
 */
data class HttpResponse<T>(val errorCode: Int, val errorMsg: String, val data: T)
