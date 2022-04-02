package cn.liyuyu.oneclipwiping.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.liyuyu.oneclipwiping.MainViewModel

/**
 * Created by frank on 2022/3/21.
 */
@Composable
fun SettingsScreen(navController: NavController?, viewModel: MainViewModel?) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("通知权限", fontSize = 20.sp)
                Text("前台服务提高存活几率")
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("自启动",fontSize = 20.sp)
                Text("自启动后台服务")
            }
        }
    }
}

@Preview
@Composable
fun SettingsPreview() {
    SettingsScreen(navController = null, viewModel = null)
}