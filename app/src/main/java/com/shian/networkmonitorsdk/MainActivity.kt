package com.shian.networkmonitorsdk

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shian.networkmonitorsdk.bean.HttpResponse
import com.shian.networkmonitorsdk.net.ApiRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private lateinit var mTv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTv = findViewById(R.id.my_tv)
    }


    private fun sendRequest(url: String) {
        val request = Request.Builder().url(url).build()
        Log.e("==========================", "OkHttp开始请求")
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val string = response.body().string()
                runOnUiThread {
                    mTv.text = string
                }

            }
        })
    }

    private fun getBanner() {
        Log.e("==========================", "Retrofit开始请求")
        ApiRepository.banner().enqueue(object : Callback<HttpResponse<Any>> {
            override fun onResponse(
                call: Call<HttpResponse<Any>>, response: Response<HttpResponse<Any>>
            ) {
                mTv.text = response.body().data.toString()
            }

            override fun onFailure(call: Call<HttpResponse<Any>>, t: Throwable) {}
        })
    }

    fun getBanner(view: View) {
        getBanner()
//        sendRequest("https://www.wanandroid.com/article/list/0/json")
    }
}