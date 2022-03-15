package cn.liyuyu.oneclipwiping.ui.screen

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Switch
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import cn.liyuyu.oneclipwiping.service.GuardAccessibilityService
import cn.liyuyu.oneclipwiping.service.GuardForegroundService
import cn.liyuyu.oneclipwiping.utils.OnLifecycleEvent
import cn.liyuyu.oneclipwiping.utils.StateUtil

/**
 * Created by frank on 2022/3/14.
 */
@Composable
fun MainScreen() {
    var isRunning by remember { mutableStateOf(false) }
    val context = LocalContext.current
    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                isRunning = StateUtil.isGasEnabled(context)
            }
            else -> {}
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Switch(
            checked = isRunning, onCheckedChange = {
                if (isRunning) {
                    GuardAccessibilityService.instance?.disableSelf()
                    val intent = Intent(context, GuardForegroundService::class.java)
                    context.stopService(intent)
                    isRunning = false
                } else {
                    StateUtil.gotoAccessibilityServiceSettings(context)
                    val intent = Intent(context, GuardForegroundService::class.java)
                    context.startService(intent)
                }
            }
        )
    }
}
