package cn.liyuyu.oneclipwiping.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.graphics.PixelFormat
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import cn.liyuyu.oneclipwiping.utils.ClipboardUtil
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by frank on 2022/3/14.
 */
class GuardAccessibilityService : AccessibilityService() {

    companion object {
        var instance: GuardAccessibilityService? = null
    }

    private var windowManager: WindowManager? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        ClipboardUtil.startListen(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager = null
        instance = null
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        when (event?.eventType) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                if (event.text.joinToString().contains("粘贴")) {
                    clearClipboard()
                }
            }
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                if (event.className == "android.widget.Toast" && event.text.joinToString()
                        .contains("已复制")
                ) {
//                    MainScope().launch {
//                        delay(10000)
//                        clearClipboard()
//                    }
                }
            }
        }
    }

    override fun onInterrupt() {
    }

    fun waitClearClipboard(){
        MainScope().launch {
            delay(5000)
            clearClipboard()
        }
    }

    /**
     * 开启悬浮窗获取焦点，清空剪贴板
     */
    private fun clearClipboard() {
        var shadowView: View? = View(this)
        val layoutParams = WindowManager.LayoutParams(
            1,
            1,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSPARENT
        )
        val listener = object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
                ClipboardUtil.clear(this@GuardAccessibilityService)
                windowManager?.removeView(shadowView)
                shadowView?.removeOnAttachStateChangeListener(this)
                shadowView = null
            }

            override fun onViewDetachedFromWindow(p0: View?) {
            }

        }
        shadowView?.addOnAttachStateChangeListener(listener)
        windowManager?.addView(shadowView, layoutParams)
    }

}