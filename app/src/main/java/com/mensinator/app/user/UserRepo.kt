package com.mensinator.app.user


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


//Registeration and sign- in will be created

//parameters for handling authentication and data base operation
class UserRepository(private val auth: FirebaseAuth,
                     private val firestore: FirebaseFirestore
) {

    // return a result object indicating success or failure
    suspend fun signUp(email: String, password: String, firstName: String, lastName: String): Result<Boolean> =
        try {
            //creates a user with a firebase authentication
            //.await() - KOTLIN COROUTINE - used to supend fnn until the result is available
            //- execution of the code is paused until the result(w/o blocking the main thread)
            auth.createUserWithEmailAndPassword(email, password).await()
            // a user object is created  with the info
            val user = User(firstName, lastName, email)
            //adds user to a firebase
            saveUserToFirestore(user)
            //returns true if thr result is success
            Result.Success(true)
        } catch (e: Exception) {
            //returns an object containing the exception
            Result.Error(e)
        }


    suspend fun login(email: String, password: String): Result<Boolean> =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    //user name parameter is being passed which is of type User(email,firstname, lastname)
    private suspend fun saveUserToFirestore(user: User) {
        //access the firebase collection named 'users'
        //creates or access  a document with the id specified by the 'user.email'
        //sets the document data to the 'user' object
        //await() suspends the coroutine until the firebase operation is completed
        firestore.collection("users").document(user.email).set(user).await()
    }

    suspend fun getCurrentUser(): Result<User> = try {
        val uid = auth.currentUser?.email
        if (uid != null) {
            val userDocument = firestore.collection("users").document(uid).get().await()    //fetches the doc of uid
            val user = userDocument.toObject(User::class.java)      //conerting "docsnapshot" obj -> user class object
            if (user != null) {
                Result.Success(user)
            } else {
                Result.Error(Exception("User data not found"))
            }
        } else {
            Result.Error(Exception("User not authenticated"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }
}
