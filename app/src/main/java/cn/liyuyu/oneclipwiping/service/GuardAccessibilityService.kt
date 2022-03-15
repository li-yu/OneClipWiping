package cn.liyuyu.oneclipwiping.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import cn.liyuyu.oneclipwiping.utils.ClipboardUtil
import kotlinx.coroutines.*


/**
 * Created by frank on 2022/3/14.
 */
class GuardAccessibilityService : AccessibilityService() {
    companion object {
        var instance: GuardAccessibilityService? = null
        var windowManager: WindowManager? = null
        var shadowView: View? = null
        val scope = MainScope()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Handler(Looper.getMainLooper()).postDelayed({
            windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager = null
        shadowView = null
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
                    waitThenClear()
                }
            }
        }
    }

    override fun onInterrupt() {
    }

    /**
     * 开启悬浮窗获取焦点，清空剪贴板
     */
    private fun clearClipboard() {
        if (shadowView == null) {
            shadowView = View(this)
        }
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSPARENT
        )

        shadowView?.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View?) {
                ClipboardUtil.clear(this@GuardAccessibilityService)
                windowManager?.removeView(shadowView)
            }

            override fun onViewDetachedFromWindow(p0: View?) {
            }

        })
        windowManager?.addView(shadowView, layoutParams)
    }

    /**
     * 有一些 app 无障碍无法捕捉到粘贴事件，所以在复制的时候延时 10s 清空剪贴板
     * 10s 够不够？
     */
    private fun waitThenClear() {
        scope.launch(Dispatchers.IO) {
            delay(10000L)
            withContext(Dispatchers.Main) {
                clearClipboard()
            }
        }
    }
}