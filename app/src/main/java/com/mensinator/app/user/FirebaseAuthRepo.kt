//package com.mensinator.app.user
//
//import com.google.firebase.auth.FirebaseAuth
//
//class FirebaseAuthRepository {
//
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//
//    fun signUp(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    onResult(true, null)
//                } else {
//                    onResult(false, task.exception?.message)
//                }
//            }
//    }
//
//    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    onResult(true, null)
//                } else {
//                    onResult(false, task.exception?.message)
//                }
//            }
//    }
//
//    fun logout() {
//        auth.signOut()
//    }
//
//    fun getCurrentUser() = auth.currentUser
//}
