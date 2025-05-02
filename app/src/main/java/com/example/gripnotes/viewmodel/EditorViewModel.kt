package com.example.gripnotes.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gripnotes.model.AuthServiceI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for the Editor screen.
 *
 * @author ericwb0
 */
@HiltViewModel
class EditorViewModel @Inject constructor(auth: AuthServiceI) : ViewModel() {

}