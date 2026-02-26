package com.mensinator.app.business.notifications

import java.time.LocalDate

interface IAndroidNotificationScheduler {
    fun scheduleNotification(
        messageText: String,
        notificationDate: LocalDate
    )

    fun cancelScheduledNotification()

    /** Triggers a local notification immediately (for testing). */
    fun showTestNotification(messageText: String)
}
