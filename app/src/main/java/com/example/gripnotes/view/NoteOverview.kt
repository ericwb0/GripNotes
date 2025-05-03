package com.example.gripnotes.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gripnotes.model.Note
import com.example.gripnotes.model.NoteContentItem
import com.example.gripnotes.ui.theme.GripNotesTheme

/**
 * Note Overview composable.
 * Lists title and first line of each note.
 * Meant to be used in a grid on the Notes screen.
 *
 * @param note The note to display.
 * @param onClick Callback function to be called with the note's ID when the note is clicked.
 * @author ericwb0
 */
@Composable
fun NoteOverview(note: Note, modifier: Modifier = Modifier, onClick: (String) -> Unit = {}) {
    OutlinedCard(
        onClick = { onClick(note.id) },
        modifier = modifier.testTag("note_overview" + note.id)
    ) {
        Column (
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = note.title.ifBlank { "<No title>" },
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp),
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
            // Next we need to display the first item of the note
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                var firstLine = "No content" // So we can use only one Text composable
                if (note.content.isNotEmpty()) {
                    when (val item = note.content.first()) {
                        is NoteContentItem.TextItem -> {
                            firstLine = item.text
                        }
                        is NoteContentItem.CheckboxItem -> {
                            firstLine = item.text
                            // Put a checkbox in front of the text
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = null,
                                enabled = false // Only needed to change the colors to disabled
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
                Text(
                    text = firstLine,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}

/**
 * Note Overview preview with text.
 * Displays a sample note.
 *
 * @author ericwb0
 */
@Preview
@Composable
fun NoteOverviewPreview() {
    val note = Note(
        id = "sample_id",
        title = "Sample Note",
        content = listOf(
            NoteContentItem.TextItem("This is a sample note."),
            NoteContentItem.CheckboxItem("This is a checkbox item.", false)
        )
    )
    GripNotesTheme {
        NoteOverview(note, Modifier.width(200.dp).padding(8.dp))
    }
}

/**
 * Note Overview preview with checkbox.
 * Displays a sample note with a checkbox.
 *
 * @author ericwb0
 */
@Preview
@Composable
fun NoteOverviewCheckboxPreview() {
    val note = Note(
        id = "sample_id",
        title = "Sample Note",
        content = listOf(
            NoteContentItem.CheckboxItem("This is a checkbox item.", true)
        )
    )
    GripNotesTheme {
        NoteOverview(note, Modifier.width(200.dp).padding(8.dp))
    }
}
/**
 * Note Overview preview with no content.
 * Displays a sample note with no content.
 *
 * @author ericwb0
 */
@Preview
@Composable
fun NoteOverviewNoContentPreview() {
    val note = Note(
        id = "sample_id",
        title = "",
        content = emptyList()
    )
    GripNotesTheme {
        NoteOverview(note, Modifier.width(200.dp).padding(8.dp))
    }
}

/**
 * Note Overview preview with long items
 *
 * @author ericwb0
 */
@Preview
@Composable
fun NoteOverviewLongTitlePreview() {
    val note = Note(
        id = "sample_id",
        title = "This is a very long title that should be truncated",
        content = listOf(
            NoteContentItem.TextItem(
                "This is long string of text that should be truncated" +
                        " and not wrap to the next line. This is a long string of text that should be truncated." +
                        " This is a long string of text that should be truncated. This is a long string of text" +
                        " that should be truncated. This is a long string of text that should be truncated." +
                        " This is a long string of text that should be truncated. This is a long string of text" +
                        " that should be truncated. This is a long string of text that should be truncated." +
                        " This is a long string of text that should be truncated. This is a long string of text" +
                        " that should be truncated."
            )
        )
    )
    GripNotesTheme {
        NoteOverview(note, Modifier.width(200.dp).padding(8.dp))
    }
}