package cn.liyuyu.oneclipwiping.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import cn.liyuyu.oneclipwiping.service.GuardForegroundService
import cn.liyuyu.oneclipwiping.utils.ClipboardUtil

/**
 * Created by frank on 2022/3/14.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        context.startService(Intent(context, GuardForegroundService::class.java))
    }
}