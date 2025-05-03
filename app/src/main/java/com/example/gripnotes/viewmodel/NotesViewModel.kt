package com.example.gripnotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gripnotes.model.Note
import com.example.gripnotes.model.RepositoryI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Notes screen.
 *
 * @author ericwb0
 */
@HiltViewModel
class NotesViewModel @Inject constructor(private val db: RepositoryI): ViewModel() {

    /*
     * UI state variables
     */
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    /*
     * Live feed of all notes for the user
     */
    val notes = db.notes
        .also {
            viewModelScope.launch {

                it.collect { notes ->
                    _isLoading.value = false
                    if (notes.isEmpty()) {
                        _error.value = "No notes found"
                    } else {
                        _error.value = ""
                    }
                }
            }
        }

    fun addNote() {
        val note = Note(title = "New Note") // Create just in case we need id later
        viewModelScope.launch {
            // All we do on this screen is add an empty note, data is handled in the editor
            db.setNote(note)
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            db.deleteNote(id)
        }
    }
}