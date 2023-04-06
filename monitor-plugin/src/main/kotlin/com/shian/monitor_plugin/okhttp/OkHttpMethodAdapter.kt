package com.shian.monitor_plugin.okhttp

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

/**
 * 访问方法里面的具体代码
 */
class OkHttpMethodAdapter(
    methodVisitor: MethodVisitor?,
    access: Int,
    name: String?,
    descriptor: String?
) : AdviceAdapter(
    Opcodes.ASM7, methodVisitor, access, name, descriptor
) {

    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
        mv?.let {
            it.visitVarInsn(ALOAD, 0)
            it.visitFieldInsn(GETFIELD, "okhttp3/OkHttpClient\$Builder", "interceptors", "Ljava/util/List;")
            it.visitFieldInsn(GETSTATIC, "com/shian/monitor/hook/OkHttpHooker", "INSTANCE", "Lcom/shian/monitor/hook/OkHttpHooker;")
            it.visitMethodInsn(INVOKEVIRTUAL, "com/shian/monitor/hook/OkHttpHooker", "getHookInterceptors", "()Ljava/util/List;", false)
            it.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "addAll", "(Ljava/util/Collection;)Z", true)
            it.visitInsn(POP)
        }
    }
}