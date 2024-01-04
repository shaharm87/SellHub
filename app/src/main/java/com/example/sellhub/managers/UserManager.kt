package com.example.sellhub.managers

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserProfileChangeRequest

class UserManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerUser(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateDisplayName("test", callback)
                } else {
                    val exception = task.exception as FirebaseAuthException
                    callback(false, exception.message)
                }
            }
    }

    fun loginUser(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null) // User registered successfully
                } else {
                    val exception = task.exception as FirebaseAuthException
                    callback(false, exception.message) // Registration failed
                }
            }
    }

    fun updateDisplayName(displayName: String, callback: (Boolean, String?) -> Unit) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()

        val firebaseUser = auth.currentUser
        firebaseUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { updateTask ->
                if (updateTask.isSuccessful) {
                    callback(true, null);
                } else {
                    val exception = updateTask.exception as FirebaseAuthException
                    callback(false, exception.message)
                }
            }
    }
}