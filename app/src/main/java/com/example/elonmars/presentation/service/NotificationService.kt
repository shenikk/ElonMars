package com.example.elonmars.presentation.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.elonmars.R
import com.example.elonmars.presentation.extensions.logDebug
import com.example.elonmars.presentation.view.MainActivity

/** Сервис для запуска нотификаций с сообщением о дате полета на Марс */
class NotificationService : Service() {

    private var remoteViews: RemoteViews? = null

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_id_1"
        private const val ACTION_DISMISS_NOTIFICATION = "ACTION_DISMISS_NOTIFICATION"
    }

    override fun onCreate() {
        logDebug("onCreate() called")
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logDebug("onStartCommand() called")

        when (intent?.action) {
            ACTION_DISMISS_NOTIFICATION -> {
                stopSelf()
            }
            else -> startForeground(NOTIFICATION_ID, createNotification())
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        logDebug("onDestroy() called")
        super.onDestroy()
    }

    private fun createNotification(): Notification {
        remoteViews = RemoteViews(packageName, R.layout.cutom_notification).apply {
            createPendingIntents(this)
        }

        // open home screen
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOnlyAlertOnce(true)
            .setCustomContentView(remoteViews)
            .setContentIntent(pendingIntent) // отобразит указанную активити при нажатии на notification
            .build()
    }


    private fun createPendingIntents(remoteViews: RemoteViews) {
        // dismiss notification
        val intentDismiss = Intent(this, NotificationService::class.java).apply {
            this.action = ACTION_DISMISS_NOTIFICATION
        }
        val pendingIntentDismiss =
            PendingIntent.getService(this, 0, intentDismiss, PendingIntent.FLAG_ONE_SHOT)
        remoteViews.setOnClickPendingIntent(R.id.dismiss_button, pendingIntentDismiss)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.notification_channel_name)
            val description = getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}
