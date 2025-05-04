package com.example.gripnotes.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gripnotes.model.AuthServiceI
import com.example.gripnotes.model.RepositoryI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _deleteNotesLoading = MutableStateFlow(false)
    val deleteNotesLoading = _deleteNotesLoading.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    private val _deleteSuccess = MutableStateFlow(false)
    val deleteSuccess = _deleteSuccess.asStateFlow()

}