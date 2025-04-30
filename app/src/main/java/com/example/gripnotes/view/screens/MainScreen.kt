package com.example.gripnotes.view.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.gripnotes.R
import com.example.gripnotes.view.nav.LoggedInNavHost

/**
 * Main screen composable.
 * It serves as a scaffold for the logged-in portion of the app.
 * Contains the top app bar and content area.
 *
 * @author ericwb0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val innerNavController = rememberNavController()

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = androidx.compose.ui.Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    // Back button that only appears on editor screen
                },
                actions = {
                    // TODO: Delete button that only appears on notes screen
                    // Settings button
                    IconButton(onClick = { /* TODO: Open settings */ }) {
                        // Settings icon
                        Icon(
                            // material 3 settings icon
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            modifier = Modifier.padding(8.dp).testTag("settingsIcon"),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .testTag("mainScreenContainer")
        ) {
            // Navigation host for the logged-in user
            LoggedInNavHost(innerNavController)
        }
    }
}