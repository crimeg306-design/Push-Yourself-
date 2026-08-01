package com.example.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.R

class NotificationHelper(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_REMINDERS = "push_yourself_reminders"
        const val CHANNEL_EVENTS = "push_yourself_events"
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val remindersChannel = NotificationChannel(
                CHANNEL_REMINDERS,
                "Training Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily training briefings and quest status updates"
            }

            val eventsChannel = NotificationChannel(
                CHANNEL_EVENTS,
                "Quest Events",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Quest completions, level ups, and streak alerts"
            }

            notificationManager.createNotificationChannel(remindersChannel)
            notificationManager.createNotificationChannel(eventsChannel)
        }
    }

    fun showEventNotification(id: Int, title: String, message: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_EVENTS)
            .setSmallIcon(R.drawable.push_yourself_icon_1785567669145)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        try {
            notificationManager.notify(id, builder.build())
        } catch (_: Exception) {}
    }

    fun showReminderNotification(id: Int, title: String, message: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_REMINDERS)
            .setSmallIcon(R.drawable.push_yourself_icon_1785567669145)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        try {
            notificationManager.notify(id, builder.build())
        } catch (_: Exception) {}
    }
}
