package com.example.gripnotes.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * Generic Delete Confirmation dialog composable.
 * Prompts the user to confirm deletion.
 *
 * @author ericwb0
 */

@Composable
fun DeleteDialog(
    body: @Composable () -> Unit,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        ElevatedCard {
            Column {
                body()
                Row (
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ){
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Cancel",
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onDelete,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text(
                            text = "Delete",
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DeleteDialogPreview() {
    DeleteDialog(
        body = {
            Text(
                text = "Are you sure you want to delete this note?",
                modifier = Modifier.padding(16.dp)
            )
        },
        onDismiss = {},
        onDelete = {}
    )
}