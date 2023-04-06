package com.shian.monitor.bean


class MonitorData {

    var method: String? = null

    var url: String? = null

    var host: String? = null

    var path: String? = null

    var scheme: String? = null

    var protocol: String? = null

    var requestHeaders: String? = null

    var paramStr: String? = null

    var requestBody: String? = null

    var responseCode: Int = 0

    var responseBody: String? = null

    var responseMessage: String? = null

    var responseContentType: String? = null

    var responseContentLength: Long? = null

    var errorMsg: String? = null

}
