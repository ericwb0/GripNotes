package com.example.gripnotes.view.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gripnotes.viewmodel.EditorViewModel

/**
 * Editor screen composable.
 * The user will be able to edit a single note here.
 *
 * @author ericwb0
 */
@Composable
fun EditorScreen(noteId: String) {
    val editorViewModel: EditorViewModel = viewModel()

    val note by editorViewModel.note.collectAsStateWithLifecycle(null)
    val isLoading by editorViewModel.isLoading.collectAsStateWithLifecycle(false)
    val error by editorViewModel.error.collectAsStateWithLifecycle("")

    LaunchedEffect(noteId) {
        editorViewModel.getNoteById(noteId)
    }
}