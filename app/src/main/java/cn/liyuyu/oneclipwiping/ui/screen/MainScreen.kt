package cn.liyuyu.oneclipwiping.ui.screen

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Switch
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cn.liyuyu.oneclipwiping.service.GuardForegroundService

/**
 * Created by frank on 2022/3/14.
 */
@Composable
fun MainScreen() {
    var isRunning by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Switch(
            checked = isRunning, onCheckedChange = {
                isRunning = !isRunning
                if (isRunning) {
                    val intent = Intent(context, GuardForegroundService::class.java)
                    context.startService(intent)
                } else {
                    val intent = Intent(context, GuardForegroundService::class.java)
                    context.stopService(intent)
                }
            }
        )
    }
}
