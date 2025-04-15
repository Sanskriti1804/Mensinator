package com.mensinator.app.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Sign in with email and password
    suspend fun signIn(email: String, password: String): Result<Boolean> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            if (authResult.user != null) {
                Result.Success(true) // Sign-in successful
            } else {
                Result.Error(Exception("Sign-in failed"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Sign up new user with email and password
    suspend fun signUp(email: String, password: String): Result<Boolean> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            if (authResult.user != null) {
                Result.Success(true) // Sign-up successful
            } else {
                Result.Error(Exception("Sign-up failed"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Sign out the current user
    fun signOut() {
        auth.signOut()
    }

    // Check if the user is already logged in
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Get current user
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}
