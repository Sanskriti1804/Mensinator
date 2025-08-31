package com.mensinator.app.core.domain.model

/**
 * Domain model representing user settings
 */
data class UserSettings(
    val id: Long,
    val cycleLength: Int = 28,
    val periodLength: Int = 5,
    val showCycleNumbers: Boolean = true,
    val enableNotifications: Boolean = true,
    val notificationTime: String = "09:00",
    val theme: AppTheme = AppTheme.SYSTEM,
    val language: String = "en",
    val privacyMode: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Enum representing app theme options
 */
enum class AppTheme(val value: String, val label: String) {
    LIGHT("light", "Light"),
    DARK("dark", "Dark"),
    SYSTEM("system", "System");

    companion object {
        fun fromValue(value: String): AppTheme = values().find { it.value == value } ?: SYSTEM
    }
}
