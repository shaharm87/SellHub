package com.example.sellhub.managers

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class UserManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerUser(email: String, password: String, displayName: String, callback: (Boolean, String?) -> Unit) {
        if (email == "") {
            callback(false, "Missing email")
        } else if (password == "") {
            callback(false, "Missing password")
        } else if (displayName == "") {
            callback(false, "Missing Display Name")
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        updateDisplayName(displayName, callback)
                    } else { // error occurred during user registration
                        val exception = task.exception as FirebaseAuthException
                        when (exception.errorCode) {
                            "ERROR_INVALID_EMAIL" -> callback(false, "Invalid Email")
                            "ERROR_EMAIL_ALREADY_IN_USE" -> callback(false, "Email already exists")
                            "ERROR_WEAK_PASSWORD" -> callback(false, "Password must be at least 6 characters")
                            else -> {
                                callback(false, exception.message)
                            }
                        }
                    }
                }
        }
    }

    fun loginUser(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        if (email == "") {
            callback(false, "Missing email")
        } else if (password == "") {
            callback(false, "Missing password")
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result?.user?.displayName !== "") {
                            updateDisplayName(task.result.user?.displayName!!, callback) // User logged in successfully
                        }
                    } else { // error occurred during user logging in
                        val exception = task.exception as FirebaseAuthException
                        when (exception.errorCode) {
                            "ERROR_INVALID_CREDENTIAL" -> callback(false, "Wrong email or password")
                            "ERROR_INVALID_EMAIL" -> callback(false, "Invalid email")
                            else -> callback(false, exception.message)
                        }
                    }
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
                    val exception = updateTask.exception
                    if (exception != null) {
                        callback(false, exception.message)
                    }
                }
            }
    }

    fun isUserLogged(): Boolean {
        return auth.currentUser != null;
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getUserEmail(): String? {
        val user = auth.currentUser
        return user?.email
    }

    fun getUserDisplayName(): String? {
        val user = auth.currentUser
        return user?.displayName
    }

    fun logOut() {
        auth.signOut()
    }
}