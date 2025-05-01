package com.example.gripnotes.view.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.gripnotes.view.screens.AccountSettingsScreen
import com.example.gripnotes.view.screens.EditorScreen
import com.example.gripnotes.view.screens.NotesScreen

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
            NotesScreen(
                onEditNote = { noteId ->
                    navController.navigate(Dest.Editor(noteId))
                }
            )
        }
        composable<Dest.Editor> { backStackEntry ->
            EditorScreen(backStackEntry.toRoute<Dest.Editor>().noteId)
        }
        composable<Dest.AccountSettings> {
            AccountSettingsScreen(onLogout)
        }
    }
}