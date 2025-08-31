package com.mensinator.app.core.util

/**
 * A sealed class representing the result of an operation
 * that can either succeed with data or fail with an error
 */
sealed class Result<out T> {
    
    /**
     * Represents a successful operation with data
     */
    data class Success<T>(val data: T) : Result<T>()
    
    /**
     * Represents a failed operation with an error
     */
    data class Error(val exception: Throwable) : Result<Nothing>()
    
    /**
     * Represents a loading state
     */
    object Loading : Result<Nothing>()
    
    /**
     * Returns true if the result is a success
     */
    fun isSuccess(): Boolean = this is Success
    
    /**
     * Returns true if the result is an error
     */
    fun isError(): Boolean = this is Error
    
    /**
     * Returns true if the result is loading
     */
    fun isLoading(): Boolean = this is Loading
    
    /**
     * Returns the data if success, null otherwise
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }
    
    /**
     * Returns the data if success, throws exception if error
     */
    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw exception
        is Loading -> throw IllegalStateException("Result is still loading")
    }
    
    /**
     * Maps the success value using the given transform function
     */
    fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(exception)
        is Loading -> Loading
    }
    
    /**
     * Maps the success value using the given transform function that returns a Result
     */
    fun <R> flatMap(transform: (T) -> Result<R>): Result<R> = when (this) {
        is Success -> transform(data)
        is Error -> Error(exception)
        is Loading -> Loading
    }
    
    /**
     * Executes the given action if this is a success
     */
    fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) {
            action(data)
        }
        return this
    }
    
    /**
     * Executes the given action if this is an error
     */
    fun onError(action: (Throwable) -> Unit): Result<T> {
        if (this is Error) {
            action(exception)
        }
        return this
    }
    
    /**
     * Executes the given action if this is loading
     */
    fun onLoading(action: () -> Unit): Result<T> {
        if (this is Loading) {
            action()
        }
        return this
    }
}

/**
 * Extension function to create a success result
 */
fun <T> T.toResult(): Result<T> = Result.Success(this)

/**
 * Extension function to create an error result
 */
fun Throwable.toErrorResult(): Result<Nothing> = Result.Error(this)
