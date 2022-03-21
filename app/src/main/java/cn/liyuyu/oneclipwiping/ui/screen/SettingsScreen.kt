package cn.liyuyu.oneclipwiping.ui.screen

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import cn.liyuyu.oneclipwiping.MainViewModel
import cn.liyuyu.oneclipwiping.R
import cn.liyuyu.oneclipwiping.service.GuardAccessibilityService
import cn.liyuyu.oneclipwiping.service.GuardForegroundService
import cn.liyuyu.oneclipwiping.utils.OnLifecycleEvent
import cn.liyuyu.oneclipwiping.utils.StateUtil

/**
 * Created by frank on 2022/3/21.
 */
@Composable
fun SettingsScreen(navController: NavController, viewModel: MainViewModel) {
}
