package com.example.gripnotes.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gripnotes.view.DeleteDialog
import com.example.gripnotes.view.LogOutDialog
import com.example.gripnotes.viewmodel.AccountSettingsViewModel

/**
 * Account Settings screen composable.
 *
 * @author ericwb0
 */
@Composable
fun AccountSettingsScreen(
    viewModel: AccountSettingsViewModel = viewModel(),
    onLogout: () -> Unit
) {

    // State variables
    var logoutDialogVisible by remember { mutableStateOf(false) }
    var accountDeletionDialogVisible by remember { mutableStateOf(false) }

    // State variables from ViewModel
    val deleteLoading by viewModel.deleteAccountLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    if(logoutDialogVisible) {
        LogOutDialog(
            onDismiss = { logoutDialogVisible = false },
            onConfirm = {
                logoutDialogVisible = false
                viewModel.logOut(onLogout)
            }
        )
    }
    if(accountDeletionDialogVisible) {
        DeleteDialog(
            body = { Text(
                text = "Are you sure you want to delete your account? This action cannot be undone.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error
            )},
            onDismiss = { accountDeletionDialogVisible = false },
            onDelete = {
                accountDeletionDialogVisible = false
                viewModel.deleteAccount(onLogout)
            },
        )
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("accountSettingsScreen"),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(
            text = "Account Settings",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 64.dp)
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("logoutButton"),
            onClick = { logoutDialogVisible = true },
        ) {
            Text(
                text = "Log Out",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("deleteAccountButton"),
            onClick = { accountDeletionDialogVisible = true },
            enabled = !deleteLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                disabledContentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
        ) {
            if (deleteLoading) {
                Row(
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("deleteLoadingIndicator"),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        strokeWidth = 2.dp
                    )
                    Text(
                        text = "Deleting...",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }

            } else {
                Text(
                    text = "Delete Account",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }

        }
        if (error.isNotEmpty()) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}