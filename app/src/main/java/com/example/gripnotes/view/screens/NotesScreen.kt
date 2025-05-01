package com.example.gripnotes.view.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gripnotes.viewmodel.NotesViewModel

/**
 * Notes screen composable.
 * The Notes screen is the main screen where the user can view, select, create, and delete notes.
 *
 * @author ericwb0
 */
@Composable
fun NotesScreen(onEditNote: (String) -> Unit) {
    val notesViewModel: NotesViewModel = viewModel()
    // TODO: Implement the notes screen
}