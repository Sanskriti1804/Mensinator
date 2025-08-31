package com.mensinator.app.core.domain.repository

import com.mensinator.app.core.domain.model.UserSettings
import com.mensinator.app.core.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for user settings operations
 */
interface ISettingsRepository {
    
    /**
     * Get user settings
     */
    suspend fun getUserSettings(): Result<UserSettings>
    
    /**
     * Update user settings
     */
    suspend fun updateUserSettings(settings: UserSettings): Result<Boolean>
    
    /**
     * Get setting by key
     */
    suspend fun getSettingByKey(key: String): Result<String?>
    
    /**
     * Set setting by key
     */
    suspend fun setSettingByKey(key: String, value: String): Result<Boolean>
    
    /**
     * Get boolean setting
     */
    suspend fun getBooleanSetting(key: String, defaultValue: Boolean = false): Result<Boolean>
    
    /**
     * Set boolean setting
     */
    suspend fun setBooleanSetting(key: String, value: Boolean): Result<Boolean>
    
    /**
     * Get integer setting
     */
    suspend fun getIntegerSetting(key: String, defaultValue: Int = 0): Result<Int>
    
    /**
     * Set integer setting
     */
    suspend fun setIntegerSetting(key: String, value: Int): Result<Boolean>
    
    /**
     * Get string setting
     */
    suspend fun getStringSetting(key: String, defaultValue: String = ""): Result<String>
    
    /**
     * Set string setting
     */
    suspend fun setStringSetting(key: String, value: String): Result<Boolean>
}
