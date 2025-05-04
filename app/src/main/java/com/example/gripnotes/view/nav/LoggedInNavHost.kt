package com.example.gripnotes.view.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.gripnotes.view.screens.AccountSettingsScreen
import com.example.gripnotes.view.screens.EditorScreen
import com.example.gripnotes.view.screens.NotesScreen
import com.example.gripnotes.viewmodel.AccountSettingsViewModel
import com.example.gripnotes.viewmodel.EditorViewModel
import com.example.gripnotes.viewmodel.NotesViewModel

/**
 * Navigation graph for the logged-in user.
 * Contains all the main screens for the app.
 *
 * @param navController The NavHostController that controls logged in navigation.
 * @param onLogout A callback that is called when the user logs out.
 * @author ericwb0
 */
@Composable
fun LoggedInNavHost(navController: NavHostController, onLogout : () -> Unit) {
    NavHost(navController, startDestination = Dest.Notes) {
        composable<Dest.Notes> {
            val viewModel = hiltViewModel<NotesViewModel>()
            NotesScreen(
                viewModel = viewModel,
                onEditNote = { noteId ->
                    navController.navigate(Dest.Editor(noteId))
                }
            )
        }
        composable<Dest.Editor> { backStackEntry ->
            val viewModel = hiltViewModel<EditorViewModel>()
            EditorScreen(
                viewModel = viewModel, noteId = backStackEntry.toRoute<Dest.Editor>().noteId)
        }
        composable<Dest.AccountSettings> {
            val viewModel = hiltViewModel<AccountSettingsViewModel>()
            AccountSettingsScreen(viewModel = viewModel, onLogout)
        }
    }
}