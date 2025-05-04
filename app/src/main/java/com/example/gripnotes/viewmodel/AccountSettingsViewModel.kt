package com.example.gripnotes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gripnotes.model.AuthServiceI
import com.example.gripnotes.model.RepositoryI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Account Settings screen.
 *
 * @author ericwb0
 */
@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    private val auth: AuthServiceI, private val db: RepositoryI) : ViewModel()
{
    /**
     * UI state variables
     */
    private val _deleteAccountLoading = MutableStateFlow(false)
    val deleteAccountLoading = _deleteAccountLoading.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    /**
     * Deletes the user's account and all associated notes.
     *
     * @param onSuccess Callback function to be called when the account is successfully deleted.
     */
    fun deleteAccount(onSuccess: () -> Unit) {
        _deleteAccountLoading.value = true
        _error.value = ""
        viewModelScope.launch {
            try {
                db.deleteUser(auth.currentUserId)
            } catch (e: Exception) {
                _error.value = "Error deleting user data: ${e.message}"
                Log.e("AccountSettingsViewModel", "Error deleting user data: ${e.message}")
            }
            auth.deleteAccount(object : AuthServiceI.AuthCallback {
                override fun onSuccess(userId: String) {
                    _deleteAccountLoading.value = false
                    onSuccess()
                }

                override fun onFailure(errorMessage: String) {
                    _error.value = errorMessage
                    _deleteAccountLoading.value = false
                    Log.e("AccountSettingsViewModel", "Error deleting account: $errorMessage")
                }
            })
        }
    }
    /**
     * Logs out the current user.
     */
    fun logOut(onLogOut: () -> Unit) {
        auth.logOut()
        onLogOut()
    }
}