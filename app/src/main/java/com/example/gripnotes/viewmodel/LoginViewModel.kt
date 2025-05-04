package com.example.gripnotes.viewmodel


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.gripnotes.model.AuthServiceI
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * ViewModel for the Login screen.
 *
 * @author ericwb0
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: AuthServiceI,
    @ApplicationContext private val context: Context
): ViewModel() {

    /*
     * UI state variables
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    private val _isReady = MutableStateFlow(auth.currentUserId != null)
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
        auth.logIn(email, password, object : AuthServiceI.AuthCallback {
            override fun onSuccess(userId: String) {
                // Handle successful login
                _isLoading.value = false
                _isReady.value = true
                Toast.makeText(
                    context,
                    "Logged in successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(errorMessage: String) {
                // Handle login failure
                _isLoading.value = false
                _error.value = errorMessage
            }
        })
    }
}