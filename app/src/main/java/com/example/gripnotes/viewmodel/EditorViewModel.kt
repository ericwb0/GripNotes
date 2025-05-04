package com.example.gripnotes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gripnotes.model.Note
import com.example.gripnotes.model.NoteContentItem
import com.example.gripnotes.model.RepositoryI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Editor screen.
 *
 * @author ericwb0
 */
@HiltViewModel
class EditorViewModel @Inject constructor(private val db: RepositoryI) : ViewModel() {
    /**
     * UI state variables
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()
    private val _note = MutableStateFlow<Note?>(null)
    val note = _note.asStateFlow()

    // We don't need to listen to live updates, just load a single note
    fun getNoteById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = ""
            _note.value = db.getNoteById(id)
            if (_note.value == null) {
                _error.value = "Note not found"
            }
            _isLoading.value = false
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = ""
            if(_note.value == null) {
                _error.value = "Note not found"
            } else {
                db.setNote(_note.value!!)
            }
            _isLoading.value = false
        }
    }
    fun addContent(content: NoteContentItem = NoteContentItem.TextItem("")) {
        // Won't update in db until saveNote() is called
        if(_note.value == null) {
            _error.value = "Note not found"
        } else {
            val newList = _note.value!!.content + content
            _note.value = _note.value!!.copy(content = newList)
        }
    }
    // Meant to be called inside a forEachIndexed block
    fun removeContent(index: Int) {

        if(index < 0 || index >= _note.value!!.content.size) {
            Log.e("EditorViewModel", "Invalid index: $index")
            return
        }
        // Won't update in db until saveNote() is called
        if(_note.value == null) {
            _error.value = "Note not found"
        } else {
            val newList = _note.value!!.content.filterIndexed { i, _ -> i != index }
            _note.value = _note.value!!.copy(content = newList)
        }
    }
    fun updateContent(index: Int, content: NoteContentItem) {
        if(index < 0 || index >= _note.value!!.content.size) {
            Log.e("EditorViewModel", "Invalid index: $index")
            return
        }
        // Won't update in db until saveNote() is called
        if(_note.value == null) {
            _error.value = "Note not found"
        } else {
            val newList = _note.value!!.content.toMutableList()
            newList[index] = content
            _note.value = _note.value!!.copy(content = newList)
        }
    }
}