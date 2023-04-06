package com.shian.monitor.interceptor

import android.net.Uri
import com.google.gson.Gson
import com.shian.monitor.bean.MonitorData
import com.shian.monitor.utils.*
import com.shian.monitor.utils.PatternUtils.phonePattern
import com.shian.monitor.utils.StringUtils.mobilePhone
import com.shian.monitor.utils.isProbablyUtf8
import okhttp3.*
import okio.Buffer
import okio.GzipSource
import java.io.EOFException
import java.net.URLDecoder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.math.min


/**
 * 拦截响应信息与请求信息
 */
class MonitorInterceptor : Interceptor {

    private val maxContentLength = 5L * 1024 * 1024
    private val gson: Gson = Gson()
    override fun intercept(chain: Interceptor.Chain): Response {
        //获取当前请求
        val request = chain.request()
        val monitorData = MonitorData()
        val url = request.url().toString()

        //设置请求方式
        monitorData.method = request.method()

        //设置请求URL
        monitorData.url = url
        if (url.isNotBlank()) {
            val uri = Uri.parse(url)
            //域名
            monitorData.host = uri.host
            //路径
            monitorData.path = uri.path + if (uri.query != null) "?" + uri.query else ""
            //协议
            monitorData.scheme = uri.scheme
        }

        //设置请求头信息
        val headers = request.headers()
        val stringListMap = headers.toMultimap()
        monitorData.requestHeaders = gson.toJson(stringListMap)

        //设置响应信息
        val requestBody = request.body()
        if (requestBody != null) {
            monitorData.paramStr = getParam(requestBody)
        }
        val response: Response = chain.proceed(request)

        try {
            //协议
            monitorData.protocol = response.protocol().toString()
            //响应码
            monitorData.responseCode = response.code()
            //响应信息
            monitorData.responseMessage = response.message()
        } catch (e: Exception) {
            monitorData.errorMsg = e.toString()
        }

        try {
            when {
                requestBody == null || bodyHasUnknownEncoding(request.headers()) -> {
                }
                requestBody is MultipartBody -> {
                    var formatRequestBody = ""

                    requestBody.parts().forEach {
                        val isStream =
                            it.body().contentType()?.toString()?.contains("otcet-stream") == true
                        val key = it.headers()?.value(0)
                        formatRequestBody += if (isStream) {
                            "${key}; value=文件流\n"
                        } else {
                            val value = it.body().readString()
                            "${key}; value=${value}\n"
                        }
                    }
                    monitorData.requestBody = formatRequestBody
                }
                else -> {
                    val buffer = Buffer()
                    requestBody.writeTo(buffer)
                    val charset: Charset =
                        requestBody.contentType()?.charset(StandardCharsets.UTF_8)
                            ?: StandardCharsets.UTF_8
                    if (buffer.isProbablyUtf8()) {
                        monitorData.requestBody = buffer.readString(charset)
                    }
                }
            }
        } catch (e: Exception) {
            monitorData.errorMsg = e.toString()
        }

        try {
            val responseBody = response.body()
            responseBody?.let { body ->
                body.contentType()?.let { monitorData.responseContentType = it.toString() }
            }
            val bodyHasUnknownEncoding = bodyHasUnknownEncoding(response.headers())
            if (responseBody != null && response.promisesBody() && !bodyHasUnknownEncoding) {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                var buffer = source.buffer()

                if (bodyGzipped(response.headers())) {
                    GzipSource(buffer.clone()).use { gzippedResponseBody ->
                        buffer = Buffer()
                        buffer.writeAll(gzippedResponseBody)
                    }
                }

                val charset: Charset = responseBody.contentType()?.charset(StandardCharsets.UTF_8)
                    ?: StandardCharsets.UTF_8
                if (responseBody.contentLength() != 0L && buffer.isProbablyUtf8()) {
                    val body = readFromBuffer(buffer.clone(), charset)
                    monitorData.responseBody = body
                }
                monitorData.responseContentLength = buffer.size()
            }

        } catch (e: Exception) {
            monitorData.errorMsg = e.toString()
        }
        uploadMonitorData(monitorData)

        dealWithResponseData(monitorData)
        val myBody = ResponseBody.create(MediaType.parse("text/plain"), monitorData.responseBody)
        return response.newBuilder().body(myBody).build()


    }

    /**
     * 处理响应数据
     */
    private fun dealWithResponseData(monitorData: MonitorData) {
        val responseBody = monitorData.responseBody
        if (responseBody != null) {
            //对手机号进行脱敏
            monitorData.responseBody = encryptionPhone(responseBody)
        }
    }

    /**
     * 对手机号进行脱敏
     */
    private fun encryptionPhone(str: String): String {
        val patternGroup = PatternUtils.patternGroup(phonePattern, str)
        return str.encryptionStr(patternGroup, ::mobilePhone)
    }


    /**
     * 上传响应数据
     */
    private fun uploadMonitorData(monitorData: MonitorData) {
        monitorData.responseBody =
            "{\n" + "\t\"data\": [{\n" + "\t\t\"desc\": \"13484407109\",\n" + "\t\t\"id\": 30,\n" + "\t\t\"imagePath\": \"13484407109\",\n" + "\t\t\"isVisible\": 1,\n" + "\t\t\"order\": 2,\n" + "\t\t\"title\": \"18900000000\",\n" + "\t\t\"type\": 0,\n" + "\t\t\"url\": \"https://www.wanandroid.com/blog/show/3352\"\n" + "\t}, {\n" + "\t\t\"desc\": \"\",\n" + "\t\t\"id\": 6,\n" + "\t\t\"imagePath\": \"610423199512034417\",\n" + "\t\t\"isVisible\": 1,\n" + "\t\t\"order\": 1,\n" + "\t\t\"title\": \"我们新增了一个常用导航Tab~\",\n" + "\t\t\"type\": 1,\n" + "\t\t\"url\": \"https://www.wanandroid.com/navi\"\n" + "\t}, {\n" + "\t\t\"desc\": \"一起来做个App吧\",\n" + "\t\t\"id\": 10,\n" + "\t\t\"imagePath\": \"https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png\",\n" + "\t\t\"isVisible\": 1,\n" + "\t\t\"order\": 1,\n" + "\t\t\"title\": \"一起来做个App吧\",\n" + "\t\t\"type\": 1,\n" + "\t\t\"url\": \"https://www.wanandroid.com/blog/show/2\"\n" + "\t}],\n" + "\t\"errorCode\": 0,\n" + "\t\"errorMsg\": \"\"\n" + "}"
    }


    /**
     * 读取参数
     *
     * @param requestBody
     * @return
     */
    private fun getParam(requestBody: RequestBody): String {
        val buffer = Buffer()
        var param = ""
        try {
            requestBody.writeTo(buffer)
            param = buffer.readUtf8()
            param = URLDecoder.decode(param, "utf-8")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return param
    }

    private fun readFromBuffer(buffer: Buffer, charset: Charset?): String {
        val bufferSize = buffer.size()
        val maxBytes = min(bufferSize, maxContentLength)
        var body: String = try {
            buffer.readString(maxBytes, charset!!)
        } catch (e: EOFException) {
            "\\n\\n--- 出错了${e.message} ---"
        }
        if (bufferSize > maxContentLength) {
            body += "\\n\\n--- 内容超出最大长度 ---"
        }
        return body
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals(
            "identity", ignoreCase = true
        ) && !contentEncoding.equals("gzip", ignoreCase = true)
    }

    private fun bodyGzipped(headers: Headers): Boolean {
        return "gzip".equals(headers["Content-Encoding"], ignoreCase = true)
    }

}