package com.mensinator.app.core.util

import android.util.Log
import kotlinx.coroutines.CancellationException

/**
 * Centralized error handling system for the app
 */
object ErrorHandler {
    
    private const val TAG = "ErrorHandler"
    
    /**
     * Handle different types of errors and return user-friendly messages
     */
    fun handleError(throwable: Throwable): String {
        return when (throwable) {
            is CancellationException -> {
                // Coroutine was cancelled, don't show error to user
                ""
            }
            is NetworkException -> {
                "Network error: Please check your internet connection and try again."
            }
            is DatabaseException -> {
                "Database error: Unable to save or retrieve data. Please try again."
            }
            is ValidationException -> {
                throwable.message ?: "Invalid data provided. Please check your input."
            }
            is AuthenticationException -> {
                "Authentication failed. Please log in again."
            }
            is PermissionException -> {
                "Permission denied. Please grant the required permissions."
            }
            is FileException -> {
                "File operation failed. Please try again."
            }
            else -> {
                Log.e(TAG, "Unhandled error: ${throwable.message}", throwable)
                "An unexpected error occurred. Please try again."
            }
        }
    }
    
    /**
     * Log error for debugging purposes
     */
    fun logError(tag: String, message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            Log.e(tag, message, throwable)
        } else {
            Log.e(tag, message)
        }
    }
    
    /**
     * Log warning for debugging purposes
     */
    fun logWarning(tag: String, message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            Log.w(tag, message, throwable)
        } else {
            Log.w(tag, message)
        }
    }
    
    /**
     * Log info for debugging purposes
     */
    fun logInfo(tag: String, message: String) {
        Log.i(tag, message)
    }
    
    /**
     * Check if an error is recoverable
     */
    fun isRecoverable(throwable: Throwable): Boolean {
        return when (throwable) {
            is CancellationException -> false
            is NetworkException -> true
            is DatabaseException -> true
            is ValidationException -> true
            is AuthenticationException -> true
            is PermissionException -> true
            is FileException -> true
            else -> false
        }
    }
    
    /**
     * Get error category for analytics or logging
     */
    fun getErrorCategory(throwable: Throwable): ErrorCategory {
        return when (throwable) {
            is NetworkException -> ErrorCategory.NETWORK
            is DatabaseException -> ErrorCategory.DATABASE
            is ValidationException -> ErrorCategory.VALIDATION
            is AuthenticationException -> ErrorCategory.AUTHENTICATION
            is PermissionException -> ErrorCategory.PERMISSION
            is FileException -> ErrorCategory.FILE
            else -> ErrorCategory.UNKNOWN
        }
    }
}

/**
 * Custom exception classes for different error types
 */
sealed class AppException protected constructor(message: String, cause: Throwable? = null) : Exception(message, cause)

class NetworkException(message: String, cause: Throwable? = null) : AppException(message, cause)
class DatabaseException(message: String, cause: Throwable? = null) : AppException(message, cause)
class ValidationException(message: String, cause: Throwable? = null) : AppException(message, cause)
class AuthenticationException(message: String, cause: Throwable? = null) : AppException(message, cause)
class PermissionException(message: String, cause: Throwable? = null) : AppException(message, cause)
class FileException(message: String, cause: Throwable? = null) : AppException(message, cause)

/**
 * Error categories for analytics and logging
 */
enum class ErrorCategory {
    NETWORK,
    DATABASE,
    VALIDATION,
    AUTHENTICATION,
    PERMISSION,
    FILE,
    UNKNOWN
}

/**
 * Extension function to convert any exception to AppException
 */
fun Throwable.toAppException(): AppException {
    return when (this) {
        is AppException -> this
        is CancellationException -> throw this
        else -> {
            // Create a generic AppException using one of the concrete implementations
            when {
                message?.contains("network", ignoreCase = true) == true -> 
                    NetworkException(message ?: "Network error", this)
                message?.contains("database", ignoreCase = true) == true -> 
                    DatabaseException(message ?: "Database error", this)
                message?.contains("validation", ignoreCase = true) == true -> 
                    ValidationException(message ?: "Validation error", this)
                message?.contains("auth", ignoreCase = true) == true -> 
                    AuthenticationException(message ?: "Authentication error", this)
                message?.contains("permission", ignoreCase = true) == true -> 
                    PermissionException(message ?: "Permission error", this)
                message?.contains("file", ignoreCase = true) == true -> 
                    FileException(message ?: "File error", this)
                else -> ValidationException(message ?: "Unknown error", this)
            }
        }
    }
}

/**
 * Extension function to safely execute a block and handle errors
 */
suspend fun <T> safeCall(
    errorMessage: String = "An error occurred",
    block: suspend () -> T
): Result<T> {
    return try {
        Result.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Log.e("SafeCall", errorMessage, e)
        Result.Error(e.toAppException())
    }
}
