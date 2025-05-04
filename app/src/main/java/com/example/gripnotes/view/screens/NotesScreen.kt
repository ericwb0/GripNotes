package com.example.gripnotes.view.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role.Companion.Switch
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gripnotes.model.Note
import com.example.gripnotes.view.DeleteDialog
import com.example.gripnotes.view.NoteOverview
import com.example.gripnotes.viewmodel.NotesViewModel

/**
 * Notes screen composable.
 * The Notes screen is the main screen where the user can view, select, create, and delete notes.
 *
 * @author ericwb0
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotesScreen(
    onEditNote: (String) -> Unit,
    viewModel: NotesViewModel = viewModel()
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle(emptyList())

    var deleteActive by remember { mutableStateOf(false) }
    var deleteMode by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<String?>(null) }

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)
    val error by viewModel.error.collectAsStateWithLifecycle("")

    if(deleteActive) {
        DeleteDialog(
            body = {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Are you sure you want to delete the selected note?",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            onDismiss = {
                deleteMode = false
                deleteActive = false
            },
            onDelete = {
                deleteMode = false
                deleteActive = false
                if (noteToDelete != null) {
                    viewModel.deleteNote(noteToDelete!!)
                } else {
                    Log.e("NotesScreen", "Note to delete is null")
                }

            }
        )
    }
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.testTag("add_note_fab"),
                onClick = {
                    val note = Note(title = "New Note")
                    viewModel.addNote(note)
                    onEditNote(note.id)
                }
            ) {
                Icon(Icons.Filled.Add, "Add")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Delete Mode",
                    style = MaterialTheme.typography.titleMedium
                )
                Switch(
                    checked = deleteMode,
                    onCheckedChange = {
                        deleteMode = it
                    },
                    modifier = Modifier.testTag("delete_mode_switch").padding(8.dp)
                )
            }
            if (isLoading) {
                CircularProgressIndicator()
                Text(
                    text = "Loading notes...",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else if (error.isNotEmpty()) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                // We want minimum width of 200dp for each note
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 200.dp),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),

                    ) {
                    items(notes.size) { index ->
                        val note = notes[index]
                        NoteOverview(
                            note = note,
                            onClick = { id ->
                                if(deleteMode) {
                                    noteToDelete = id
                                    deleteActive = true
                                } else {
                                    onEditNote(id)
                                }
                            },
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                }
            }
        }
    }
}