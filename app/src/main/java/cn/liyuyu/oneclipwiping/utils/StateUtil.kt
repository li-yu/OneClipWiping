package cn.liyuyu.oneclipwiping.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.core.accessibilityservice.AccessibilityServiceInfoCompat

/**
 * Created by frank on 2022/3/15.
 */
object StateUtil {

    /**
     * 判断无障碍服务是否开启
     */
    fun isGasEnabled(context: Context): Boolean {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices =
            am.getEnabledAccessibilityServiceList(AccessibilityServiceInfoCompat.FEEDBACK_ALL_MASK)
        for (info in enabledServices) {
            if (info.id.contains(context.packageName)) {
                return true
            }
        }
        return false
    }

    /**
     * 前往无障碍服务设置页面
     */
    fun gotoAccessibilityServiceSettings(context: Context) {
        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }
}