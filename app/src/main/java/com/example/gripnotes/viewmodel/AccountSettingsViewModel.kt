package com.example.gripnotes.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gripnotes.model.AuthServiceI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for the Account Settings screen.
 *
 * @author ericwb0
 */
@HiltViewModel
class AccountSettingsViewModel @Inject constructor(private val auth: AuthServiceI) : ViewModel() {

}