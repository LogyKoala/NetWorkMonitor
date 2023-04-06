package com.shian.monitor_plugin.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppExtension
import com.shian.monitor_plugin.okhttp.OkHttpTransform

class MonitorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        try {
            val appExtension: AppExtension = project.extensions.getByName("android") as AppExtension
            // 注册transform
            appExtension.registerTransform(OkHttpTransform(project))
        } catch (e: Exception) {

        }
    }
}