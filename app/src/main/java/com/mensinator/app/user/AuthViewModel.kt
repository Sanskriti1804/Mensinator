package com.mensinator.app.user


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

//authentication related logic
//FirebaseAuth.getInstance() - for managing user authentication
//Injection.instance()
class AuthViewModel : ViewModel() {
    private val userRepository: UserRepository = UserRepository(
        FirebaseAuth.getInstance(),
//       Injection.instance()
        FirebaseFirestore.getInstance()
    )

    init {
        //initializing userRepo class
    }
    //mutable live data -
    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    fun signUp(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            //runs a coroutine
            _authResult.value = userRepository.signUp(email, password, firstName, lastName)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = userRepository.login(email, password)
        }
    }
}
