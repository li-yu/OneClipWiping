package cn.liyuyu.oneclipwiping.service

import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import cn.liyuyu.oneclipwiping.MainActivity
import cn.liyuyu.oneclipwiping.R

/**
 * Created by frank on 2022/3/14.
 */
class GuardForegroundService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private val foregroundService: ForegroundNotification by lazy {
        ForegroundNotification(this)
    }

    override fun onCreate() {
        super.onCreate()
        foregroundService.startForegroundNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (null == intent) {
            return START_NOT_STICKY
        }
        foregroundService.startForegroundNotification()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        foregroundService.stopForegroundNotification()
        super.onDestroy()
    }
}

class ForegroundNotification(private val service: GuardForegroundService) :
    ContextWrapper(service) {

    companion object {
        private const val START_ID = 2140
        private const val CHANNEL_ID = "app_foreground_service"
        private const val CHANNEL_NAME = "一剪没前台保活服务"
    }

    private var mNotificationManager: NotificationManager? = null

    private var mCompatBuilder: NotificationCompat.Builder? = null

    private val compatBuilder: NotificationCompat.Builder?
        get() {
            if (mCompatBuilder == null) {
                val notificationIntent = Intent(this, MainActivity::class.java)
                notificationIntent.action = Intent.ACTION_MAIN
                notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                notificationIntent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                var pendingIntent: PendingIntent? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    pendingIntent = PendingIntent.getActivity(
                        this,
                        2140,
                        notificationIntent,
                        PendingIntent.FLAG_MUTABLE
                    )
                } else {
                    pendingIntent = PendingIntent.getActivity(
                        this, 2140,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }

                val notificationBuilder: NotificationCompat.Builder =
                    NotificationCompat.Builder(this, CHANNEL_ID)
                notificationBuilder.setContentTitle(getString(R.string.notification_title))
                notificationBuilder.setContentText(getString(R.string.notification_sub_title))
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                notificationBuilder.setContentIntent(pendingIntent)
                mCompatBuilder = notificationBuilder
            }
            return mCompatBuilder
        }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.setShowBadge(false)
            mNotificationManager?.createNotificationChannel(channel)
        }
    }

    fun startForegroundNotification() {
        service.startForeground(START_ID, compatBuilder?.build())
    }

    fun stopForegroundNotification() {
        mNotificationManager?.cancelAll()
        service.stopForeground(true)
    }
}