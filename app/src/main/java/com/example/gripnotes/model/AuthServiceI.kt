package com.example.gripnotes.model

/**
 * Interface for authentication service.
 * Main implementation is FirebaseAuthService.
 *
 * @property currentUserId A String representing the current user's ID.
 * @author ericwb0
 */
interface AuthServiceI {
    interface AuthCallback {
        fun onSuccess(userId: String)
        fun onFailure(errorMessage: String)
    }

    val currentUserId: String // Will get user details in FirestoreRepository, not here

    fun signUp(email: String, password: String, callback: AuthCallback)
    fun logIn(email: String, password: String, callback: AuthCallback)
    fun logOut()
    fun deleteAccount(callback: AuthCallback)
}