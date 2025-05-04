package com.example.gripnotes.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gripnotes.model.NoteContentItem
import com.example.gripnotes.view.EditorFAB
import com.example.gripnotes.viewmodel.EditorViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.ExperimentalTime

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
    var deleteMode by remember { mutableStateOf(false) }

    val note by viewModel.note.collectAsStateWithLifecycle(null)
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)
    val error by viewModel.error.collectAsStateWithLifecycle("")

    val focusManager = LocalFocusManager.current // Just makes the editor screen a bit more user friendly

    // Load the note when the screen is first displayed
    LaunchedEffect(noteId) {
        viewModel.getNoteById(noteId)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            },
        floatingActionButton = { EditorFAB(
            onAddTextItem = {
                viewModel.addContent(NoteContentItem.TextItem(""))
            },
            onAddCheckboxItem = {
                viewModel.addContent(NoteContentItem.CheckboxItem("", false))
            }
        )},
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                    focusManager.clearFocus()
                    viewModel.saveNote()
                }
            ) {
                Text(
                    text = "Save Note",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 0.dp, bottom = paddingValues.calculateBottomPadding(),
                    start = 4.dp, end = 4.dp
                ),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
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
                    modifier = Modifier
                        .testTag("delete_mode_switch")
                        .padding(8.dp)
                )
            }
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
                BasicTextField(
                    value = note!!.title,
                    onValueChange = { viewModel.updateTitle(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                HorizontalDivider(
                    modifier = Modifier.padding(8.dp),
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Created: ${formatTimestamp(note!!.created)}\n" +
                            "Last Modified: ${formatTimestamp(note!!.updated)}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = FontStyle.Italic
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    items(note!!.content.size) { index ->
                        Row(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            when (val contentItem = note!!.content[index]) {
                                is NoteContentItem.TextItem -> {
                                    var text by remember { mutableStateOf(contentItem.text) }
                                    BasicTextField(
                                        value = text,
                                        onValueChange = {
                                            text = it
                                            viewModel.updateContent(index, NoteContentItem.TextItem(it))
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(
                                                start = 8.dp,
                                                end = 4.dp,
                                                top = 4.dp,
                                                bottom = 4.dp
                                            ),
                                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        enabled = !deleteMode,
                                    )

                                }
                                is NoteContentItem.CheckboxItem -> {
                                    var text by remember { mutableStateOf(contentItem.text) }
                                    Row(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(4.dp),
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Checkbox(
                                            checked = contentItem.isChecked,
                                            onCheckedChange = { isChecked ->
                                                viewModel.updateContent(
                                                    index,
                                                    NoteContentItem.CheckboxItem(contentItem.text, isChecked)
                                                )
                                            },
                                        )
                                        BasicTextField(
                                            value = text,
                                            onValueChange = {
                                                text = it
                                                viewModel.updateContent(index,
                                                    NoteContentItem.CheckboxItem(it, contentItem.isChecked))
                                            },
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(
                                                    start = 4.dp,
                                                    end = 4.dp,
                                                    top = 8.dp,
                                                    bottom = 4.dp
                                                ),
                                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        )
                                    }
                                }
                            }
                            if(deleteMode) {
                                IconButton(
                                    onClick = {
                                        viewModel.removeContent(index)
                                    },
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .testTag("delete_button_$index")
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.error,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Formats a timestamp into a human-readable string.
 */
fun formatTimestamp(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return dateTime.format(formatter)
}