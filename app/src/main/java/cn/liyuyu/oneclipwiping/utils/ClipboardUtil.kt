package cn.liyuyu.oneclipwiping.utils

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import cn.liyuyu.oneclipwiping.BuildConfig
import cn.liyuyu.oneclipwiping.interceptor.ClipSnapshot
import cn.liyuyu.oneclipwiping.service.GuardAccessibilityService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by frank on 2022/3/14.
 */
object ClipboardUtil {

    fun listenClipChanged(context: Context, scope: CoroutineScope) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.addPrimaryClipChangedListener {
            Log.d("frank", "ignored")
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_LOGS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            scope.launch(Dispatchers.IO) {
                try {
                    val timeStamp: String =
                        SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss.SSS",
                            Locale.getDefault()
                        ).format(Date())
                    val process = Runtime.getRuntime().exec(
                        arrayOf(
                            "logcat",
                            "-T",
                            timeStamp,
                            "ClipboardService:E",
                            "*:S"
                        )
                    )
                    val bufferedReader = BufferedReader(
                        InputStreamReader(
                            process.inputStream
                        )
                    )
                    var line: String? = ""
                    while (line != null) {
                        line = bufferedReader.readLine()
                        if (line?.contains(BuildConfig.APPLICATION_ID) == true) {
                            GuardAccessibilityService.instance?.waitCleanClipboard()
                        }
                    }
                } catch (ignored: Exception) {
                }
            }
        } else {
            Toast.makeText(context.applicationContext, "缺少 READ_LOGS 权限！", Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * 获取当前剪贴板的快照
     */
    fun snapshoot(context: Context): ClipSnapshot? {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        with(clipboardManager) {
            return if (hasPrimaryClip()) {
                val text = primaryClip?.getItemAt(0)?.coerceToText(context).toString()
                ClipSnapshot(
                    System.currentTimeMillis(),
                    text,
                    GuardAccessibilityService.instance?.topPackageName
                )
            } else {
                null
            }

        }

    }

    fun clean(context: Context) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        with(clipboardManager) {
            if (hasPrimaryClip()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    clearPrimaryClip()
                } else {
                    setPrimaryClip(ClipData.newPlainText(null, null))
                }
                Toast.makeText(context.applicationContext, "呼~", Toast.LENGTH_SHORT).show()
            }

        }

    }
}