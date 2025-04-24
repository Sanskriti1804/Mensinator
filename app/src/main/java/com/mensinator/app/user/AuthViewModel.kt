//package com.mensinator.app.user
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//
//
//class AuthViewModel(
//    private val repository: FirebaseAuthRepository = FirebaseAuthRepository()
//) : ViewModel() {
//
//    var email by mutableStateOf("")
//    var password by mutableStateOf("")
//    var isLoading by mutableStateOf(false)
//    var errorMessage by mutableStateOf<String?>(null)
//    var isSuccess by mutableStateOf(false)
//
//    fun login() {
//        isLoading = true
//        errorMessage = null
//        repository.login(email, password) { success, error ->
//            isLoading = false
//            isSuccess = success
//            errorMessage = error
//        }
//    }
//
//    fun signUp() {
//        isLoading = true
//        errorMessage = null
//        repository.signUp(email, password) { success, error ->
//            isLoading = false
//            isSuccess = success
//            errorMessage = error
//        }
//    }
//}
