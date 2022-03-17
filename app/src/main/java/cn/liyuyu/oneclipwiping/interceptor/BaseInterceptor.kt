package cn.liyuyu.oneclipwiping.interceptor

/**
 * Created by frank on 2022/3/17.
 */
interface BaseInterceptor {
    fun process(snapshot: ClipSnapshot): Boolean
}