package com.mensinator.app.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    // MutableLiveData to observe the authentication result
    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> = _authResult

    // Function to handle login
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = Result.Loading // Show loading state
            _authResult.value = userRepository.signIn(email, password)
        }
    }

    // Function to handle sign-up
    fun signUp(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            _authResult.value = Result.Loading // Show loading state
            _authResult.value = userRepository.signUp(email, password)
        }
    }

    // Check if user is already logged in
    fun checkIfUserLoggedIn() {
        if (userRepository.isUserLoggedIn()) {
            // User is logged in, navigate to another screen or update UI accordingly
        } else {
            // Prompt for login
        }
    }
}
