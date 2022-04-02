package cn.liyuyu.oneclipwiping.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.graphics.PixelFormat
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

    private val scope = MainScope()

    private var windowManager: WindowManager? = null

    @Volatile
    private var hasClip = false

    var topPackageName: String? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        ClipboardUtil.listenClipChanged(this, scope)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager = null
        instance = null
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        when (event?.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                topPackageName = event.packageName.toString()
            }
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                if (event.text.joinToString().contains("粘贴")) {
                    cleanClipboard()
                }
            }
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                if (event.className == "android.widget.Toast") {
                    val keys = event.text.joinToString()
                    if (keys.contains("复制") || keys.contains("剪切")) {
                        waitCleanClipboard()
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
    }

    @Synchronized
    fun waitCleanClipboard() {
        hasClip = true
        scope.launch {
            delay(15000)
            if (hasClip) {
                cleanClipboard()
            }
        }
    }

    /**
     * 开启悬浮窗获取焦点，清空剪贴板
     */
    @Synchronized
    private fun cleanClipboard() {
        callClipboard {
            ClipboardUtil.clean(this@GuardAccessibilityService)
            hasClip = false
        }
    }

    /**
     * 只有等悬浮窗获取焦点后才能拿到剪贴板操作
     */
    private fun callClipboard(block: () -> Unit) {
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
                block()
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