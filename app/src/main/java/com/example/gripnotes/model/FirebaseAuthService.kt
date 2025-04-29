package com.example.gripnotes.model

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * FirebaseAuthService is responsible for handling authentication using Firebase Authentication.
 * It implements the IAuthService interface to provide methods for signing up, logging in,
 * logging out, and deleting user accounts.
 *
 * @author ericwb0
 */
class FirebaseAuthService : IAuthService {
    override val currentUserId: Flow<String>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                this.trySend(auth.currentUser?.uid ?: "")
            }
            Firebase.auth.addAuthStateListener(listener)
            awaitClose {
                Firebase.auth.removeAuthStateListener(listener)
            }
        }

    override fun signUp(email: String, password: String, callback: IAuthService.AuthCallback) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback.onSuccess(Firebase.auth.currentUser?.uid ?: "")
                } else {
                    callback.onFailure(task.exception?.message ?: "Unknown: Sign up failed")
                }
            }
    }
    override fun logIn(email: String, password: String, callback: IAuthService.AuthCallback) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback.onSuccess(Firebase.auth.currentUser?.uid ?: "")
                } else {
                    callback.onFailure(task.exception?.message ?: "Unknown: Login failed")
                }
            }
    }
    override fun logOut() {
        Firebase.auth.signOut()
    }

    override fun deleteAccount(callback: IAuthService.AuthCallback) {
        val user = Firebase.auth.currentUser

        if (user != null) {
            val id = user.uid
            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callback.onSuccess(id)
                    } else {
                        callback.onFailure(task.exception?.message ?: "Unknown: Account deletion failed")
                    }
                }
        } else {
            callback.onFailure("No user is currently logged in.")
        }
    }
}