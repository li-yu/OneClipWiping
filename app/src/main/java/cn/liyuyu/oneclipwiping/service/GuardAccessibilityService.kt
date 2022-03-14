package cn.liyuyu.oneclipwiping.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import cn.liyuyu.oneclipwiping.utils.ClipboardUtil


/**
 * Created by frank on 2022/3/14.
 */
class GuardAccessibilityService : AccessibilityService() {
    companion object {
        var INS: GuardAccessibilityService? = null
        var wm: WindowManager? = null
        var shadow: View? = null
    }

    override fun onCreate() {
        super.onCreate()
        INS = this
        Handler().postDelayed({
            wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }, 1000)
    }

    override fun onDestroy() {
        INS = null
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        when (event?.eventType) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                Log.d("fuckliyu", event.toString())
                Log.d("fuckliyu", event.text.toString())
                if (event.text.contains("粘贴")) {
                    startFloat()
                }
            }
        }
    }

    override fun onInterrupt() {
    }


    fun startFloat() {
        shadow = View(INS!!)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        layoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        layoutParams.width = 100
        layoutParams.height = 100
        shadow?.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View?) {
                ClipboardUtil.clear(INS!!)
                wm?.removeView(shadow)
            }

            override fun onViewDetachedFromWindow(p0: View?) {
                Log.d("ssss", "ddd")
            }

        })
        wm?.addView(shadow, layoutParams)
    }
}