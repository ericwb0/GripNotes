package com.example.gripnotes.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gripnotes.model.AuthServiceI
import com.example.gripnotes.model.RepositoryI
import com.example.gripnotes.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Sign Up screen.
 *
 * @author ericwb0
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(auth: AuthServiceI, db: RepositoryI): ViewModel() {
    private val _auth = auth
    private val _db = db

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    /**
     * Validates the email format using a regex pattern.
     */
    private fun validateEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        return email.trim().matches(emailPattern.toRegex())
    }

    /**
     * Attempts to sign up a new user with the provided email and password.
     * Passes error messages to the UI if any validation fails.
     *
     * @param email The email address of the user.
     * @param password The password for the user.
     * @param confirmPass The confirmation password for the user which must match password.
     */
    fun signUp(email: String, password: String, confirmPass: String) {
        _isReady.value = false
        _error.value = ""

        // Basic input validation
        when{
            email.isEmpty() || password.isEmpty() || confirmPass.isEmpty() -> {
                _error.value = "Email cannot be empty"
                return
            }
            !validateEmail(email) -> {
                _error.value = "Invalid email format"
                return
            }
            password.length < 6 -> {
                _error.value = "Password must be at least 6 characters"
                return
            }
            password != confirmPass -> {
                _error.value = "Passwords do not match"
                return
            }
        }

        // Attempt to sign up the user
        viewModelScope.launch {
            _isLoading.value = true
            _auth.signUp(email, password, object : AuthServiceI.AuthCallback {
                override fun onSuccess(userId: String) {
                    _isLoading.value = false
                    // Handle successful sign up
                    viewModelScope.launch {
                        _db.setUser(User(id = userId, email = email))
                    }
                    _isReady.value = true
                }

                override fun onFailure(errorMessage: String) {
                    // Error message from Firebase Auth will be passed to UI
                    _isLoading.value = false
                    _error.value = errorMessage
                }
            })
        }
    }
}