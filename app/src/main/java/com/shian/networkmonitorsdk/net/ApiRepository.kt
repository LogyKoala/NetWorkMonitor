package com.shian.networkmonitorsdk.net

import com.shian.networkmonitorsdk.bean.HttpResponse
import com.shian.networkmonitorsdk.net.api.ApiServer
import retrofit2.Call

object ApiRepository {
    private val apiServer: ApiServer = RetrofitServiceManager.createRetrofitService(
        "https://www.wanandroid.com", ApiServer::class.java
    )

    fun banner(): Call<HttpResponse<Any>> {
        return apiServer.banner()
    }
}