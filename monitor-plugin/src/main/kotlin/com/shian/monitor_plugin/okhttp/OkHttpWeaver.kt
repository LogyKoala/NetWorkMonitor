package com.shian.monitor_plugin.okhttp

import com.quinn.hunter.transform.asm.BaseWeaver
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

//BaseWeaver 里面处理了ASM的很多复杂逻辑
class OkHttpWeaver : BaseWeaver() {
    override fun wrapClassWriter(classWriter: ClassWriter?): ClassVisitor {
        return OkHttpClassVisitor(classWriter)
    }
}