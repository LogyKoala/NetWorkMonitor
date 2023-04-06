package com.shian.networkmonitorsdk.net.api

import com.shian.networkmonitorsdk.bean.HttpResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiServer {
    /**
     * 获取banner
     */
    @GET("/banner/json")
    fun banner(): Call<HttpResponse<Any>>
}