package com.example.gripnotes.view.screens

import androidx.compose.runtime.Composable
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
    // TODO: Implement the editor screen
}