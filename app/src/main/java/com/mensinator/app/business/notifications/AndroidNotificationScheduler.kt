package com.mensinator.app.business.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.mensinator.app.NotificationReceiver
import java.time.LocalDate
import java.time.ZoneId

/**
 * Allows scheduling/cancelling notifications on Android.
 */
class AndroidNotificationScheduler(
    private val context: Context,
    private val alarmManager: AlarmManager,
) : IAndroidNotificationScheduler {
    override fun scheduleNotification(
        messageText: String,
        notificationDate: LocalDate
    ) {
        val notificationTimeMillis =
            notificationDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            notificationTimeMillis,
            getPendingIntent(messageText)
        )
        Log.d("NotificationScheduler", "Notification scheduled for $notificationDate")
    }

    override fun cancelScheduledNotification() {
        alarmManager.cancel(getPendingIntent(messageText = null))
        Log.d("NotificationScheduler", "Notification cancelled")
    }

    override fun showTestNotification(messageText: String) {
        val pendingIntent = getPendingIntent(messageText)
        try {
            pendingIntent.send()
            Log.d("NotificationScheduler", "Test notification triggered")
        } catch (e: PendingIntent.CanceledException) {
            Log.e("NotificationScheduler", "Failed to send test notification", e)
        }
    }

    private fun getPendingIntent(messageText: String?): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_NOTIFICATION
            messageText?.let { putExtra(NotificationReceiver.MESSAGE_TEXT_KEY, it) }
        }
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}