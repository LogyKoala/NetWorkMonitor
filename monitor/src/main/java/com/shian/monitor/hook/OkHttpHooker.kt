package com.shian.monitor.hook

import com.shian.monitor.interceptor.MonitorInterceptor

object OkHttpHooker {
    //ASM修改字节码对OKHTTP进行hook用的
    val hookInterceptors = listOf(
        MonitorInterceptor()
    )
}