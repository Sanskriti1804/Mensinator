package com.mensinator.app.user


//hold the daa state for each outcome
// t - generic type (any type)
//out - out- projection modifier[only produce input no input]
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()

    //<nothing> - doesnt prodiuce any value for result
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

//usage example
// val data = "Fetched Data"  // Replace with actual data fetching logic
//        Result.Success(data)