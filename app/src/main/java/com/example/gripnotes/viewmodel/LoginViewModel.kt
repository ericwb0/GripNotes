package com.example.gripnotes.viewmodel


import androidx.lifecycle.ViewModel
import com.example.gripnotes.model.AuthServiceI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * ViewModel for the Login screen.
 *
 * @author ericwb0
 */
@HiltViewModel
class LoginViewModel @Inject constructor(auth: AuthServiceI): ViewModel() {
    private val _auth = auth

    /*
     * UI state variables
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    /**
     * Attempts to log in a user with the provided email and password.
     *
     * @param email The email address of the user.
     * @param password The password for the user.
     */
    fun logIn(email: String, password: String) {
        _isReady.value = false
        _error.value = ""
        _isLoading.value = true
        _auth.logIn(email, password, object : AuthServiceI.AuthCallback {
            override fun onSuccess(userId: String) {
                // Handle successful login
                _isLoading.value = false
                _isReady.value = true
            }

            override fun onFailure(errorMessage: String) {
                // Handle login failure
                _isLoading.value = false
                _error.value = errorMessage
            }
        })
    }
}