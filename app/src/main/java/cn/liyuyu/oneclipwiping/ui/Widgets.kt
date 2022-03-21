package cn.liyuyu.oneclipwiping.ui

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*

/**
 * Created by frank on 2022/3/21.
 */
@Composable
fun TipsDialog(
    title: String,
    msg: String,
    confirmAction: (() -> Unit)? = null,
    dismissAction: (() -> Unit)? = null
) {
    var isShow by remember { mutableStateOf(true) }
    if (isShow) {
        AlertDialog(
            onDismissRequest = { isShow = false },
            title = { Text(text = title) },
            text = { Text(text = msg) },
            confirmButton = {
                TextButton(
                    onClick = {
                        isShow = false
                        confirmAction?.invoke()
                    }
                ) {
                    Text("好的")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isShow = false
                        dismissAction?.invoke()
                    }
                ) {
                    Text("关闭")
                }
            }
        )
    }
}