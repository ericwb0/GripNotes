package com.example.gripnotes.view.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gripnotes.model.NoteContentItem
import com.example.gripnotes.view.DeleteDialog
import com.example.gripnotes.view.EditorFAB
import com.example.gripnotes.viewmodel.EditorViewModel

/**
 * Editor screen composable.
 * The user will be able to edit a single note here.
 *
 * @author ericwb0
 */
@Composable
fun EditorScreen(
    viewModel: EditorViewModel = viewModel(),
    noteId: String
) {

    var deleteActive by remember { mutableStateOf(false) }
    var deleteIndex by remember { mutableIntStateOf(-1) }

    val note by viewModel.note.collectAsStateWithLifecycle(null)
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)
    val error by viewModel.error.collectAsStateWithLifecycle("")

    // Load the note when the screen is first displayed
    LaunchedEffect(noteId) {
        viewModel.getNoteById(noteId)
    }

    if (deleteActive) {
        DeleteDialog(
            body = {
                Text(
                    text = "Are you sure you want to delete this item?",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            onDismiss = {
                deleteIndex = -1
                deleteActive = false
            },
            onDelete = {
                viewModel.removeContent(deleteIndex)
                deleteIndex = -1
                deleteActive = false
            }
        )
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = { EditorFAB(
            onAddTextItem = {
                viewModel.addContent(NoteContentItem.TextItem(""))
            },
            onAddCheckboxItem = {
                viewModel.addContent(NoteContentItem.CheckboxItem("", false))
            }
        )}
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            if (isLoading) {
                CircularProgressIndicator()
                Text(
                    text = "Loading note...",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else if (error.isNotEmpty() || note == null) {
                Text(
                    text = error.ifEmpty { "Note not found" },
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text(
                    text = note!!.title,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
                HorizontalDivider(
                    modifier = Modifier.padding(8.dp),
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    items(note!!.content.size) { index ->
                        when (val contentItem = note!!.content[index]) {
                            is NoteContentItem.TextItem -> {
                                var text by remember { mutableStateOf(contentItem.text) }
                                var isFocused by remember { mutableStateOf(false) }
                                BasicTextField(
                                    value = text,
                                    onValueChange = { text = it },
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)
                                        .onFocusChanged {
                                            isFocused = it.isFocused
                                            if (!it.isFocused) {
                                                viewModel.updateContent(index,
                                                    NoteContentItem.TextItem(text))
                                            }
                                        }.pointerInput(Unit) {
                                            detectTapGestures(
                                                onLongPress = {
                                                    deleteIndex = index
                                                    deleteActive = true
                                                }
                                            )
                                        },
                                    textStyle = MaterialTheme.typography.bodyLarge
                                )
                            }
                            is NoteContentItem.CheckboxItem -> {
                                var text by remember { mutableStateOf(contentItem.text) }
                                var isFocused by remember { mutableStateOf(false) }
                                Row(
                                    modifier = Modifier.fillMaxSize().padding(8.dp)
                                        .onFocusChanged {
                                            isFocused = it.isFocused
                                            if (!it.isFocused) {
                                                viewModel.updateContent(
                                                    index,
                                                    NoteContentItem.CheckboxItem(
                                                        text, contentItem.isChecked)
                                                )
                                            }
                                        }.pointerInput(Unit) {
                                            detectTapGestures(
                                                onLongPress = {
                                                    deleteIndex = index
                                                    deleteActive = true
                                                }
                                            )
                                        },
                                ) {
                                    Checkbox(
                                        checked = contentItem.isChecked,
                                        onCheckedChange = { isChecked ->
                                            viewModel.updateContent(
                                                index,
                                                NoteContentItem.CheckboxItem(contentItem.text, isChecked)
                                            )
                                        }
                                    )
                                    BasicTextField(
                                        value = text,
                                        onValueChange = { text = it },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(8.dp),
                                        textStyle = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
                Button(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    onClick = {
                        viewModel.saveNote()
                    }
                ) {
                    Text(text = "Save Note")
                }
            }
        }
    }

}