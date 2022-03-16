package cn.liyuyu.oneclipwiping.utils

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat
import cn.liyuyu.oneclipwiping.BuildConfig
import cn.liyuyu.oneclipwiping.service.GuardAccessibilityService
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by frank on 2022/3/14.
 */
object ClipboardUtil {

    fun startListen(context: Context) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.addPrimaryClipChangedListener {
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_LOGS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Thread {
                try {
                    val timeStamp: String =
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())
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
                    val iterator = process.inputStream.bufferedReader().lineSequence().iterator()
                    while (iterator.hasNext()) {
                        val line = iterator.next()
                        if (line.contains(BuildConfig.APPLICATION_ID)) {
                            GuardAccessibilityService.instance?.waitClearClipboard()
                        }
                    }
                } catch (ignored: Exception) {
                }
            }.start()
        }


    }

    fun clear(context: Context) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        with(clipboardManager) {
            if (clipboardManager.hasPrimaryClip()) {
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