package cn.liyuyu.oneclipwiping.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast

/**
 * Created by frank on 2022/3/14.
 */
object ClipboardUtil {

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