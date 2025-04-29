package com.example.gripnotes.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Interface for authentication service.
 * Main implementation is FirebaseAuthService.
 *
 * @author ericwb0
 */
interface IAuthService {
    interface AuthCallback {
        fun onSuccess(userId: String)
        fun onFailure(errorMessage: String)
    }

    val currentUserId: Flow<String> // Will get user details in FirestoreRepository, not here

    fun signUp(email: String, password: String, callback: AuthCallback)
    fun logIn(email: String, password: String, callback: AuthCallback)
    fun logOut()
    fun deleteAccount(callback: AuthCallback)
}