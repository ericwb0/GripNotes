package com.example.gripnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.gripnotes.ui.theme.GripNotesTheme
import com.example.gripnotes.view.nav.RootNavHost
import dagger.hilt.android.AndroidEntryPoint

/**
 * The only activity and entry point of the app.
 * All other screens will be handled by type safe Compose navigation.
 *
 * @author ericwb0
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val rootNavController = rememberNavController()
            GripNotesTheme {
                RootNavHost(rootNavController)
            }
        }
    }
}