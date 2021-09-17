package com.example.elonmars.presentation.home.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.elonmars.presentation.home.service.NotificationService

/** Ресивер, которые слушает сообщения от AlarmManager и запускает сервис */
class NotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        startService(context)
    }

    private fun startService(context: Context?) {
        val intent = Intent(context, NotificationService::class.java)
        context?.startService(intent)
    }
}
