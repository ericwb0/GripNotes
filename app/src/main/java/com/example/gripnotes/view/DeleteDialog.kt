package com.example.gripnotes.view

import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

/**
 * Generic Delete Confirmation dialog composable.
 * Prompts the user to confirm deletion of a note.
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

    }
}