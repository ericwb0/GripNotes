package com.example.gripnotes.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

/**
 * Log Out dialog composable.
 *
 * @author ericwb0
 */

@Composable
fun LogOutDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {

    }
}