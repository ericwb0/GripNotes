package com.example.gripnotes.view.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.gripnotes.view.screens.LoginScreen
import com.example.gripnotes.view.screens.MainScreen
import com.example.gripnotes.view.screens.SignUpScreen
import com.example.gripnotes.viewmodel.LoginViewModel
import com.example.gripnotes.viewmodel.SignUpViewModel

/**
 * Contains the root navigation graph.
 * Navigates between logged out and logged in destinations.
 *
 * @author ericwb0
 */
@Composable
fun RootNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = SubGraph.Auth) {
        navigation<SubGraph.Auth> (startDestination = Dest.Login) {
            composable<Dest.Login> {
                val viewModel = hiltViewModel<LoginViewModel>()
                LoginScreen(
                    viewModel = viewModel,
                    onLogin = {
                        navController.navigate(SubGraph.Main) {
                            // Clear the entire back stack
                            popUpTo(SubGraph.Auth) {
                                inclusive = true
                            }
                        }
                    },
                    onSignUp = { navController.navigate(Dest.SignUp) }
                )
            }
            composable<Dest.SignUp> {
                val viewModel = hiltViewModel<SignUpViewModel>()
                SignUpScreen(
                    viewModel = viewModel,
                    onLogin = { navController.navigate(Dest.Login) },
                    onSignUp = {
                        navController.navigate(SubGraph.Main) {
                            // Clear the entire back stack
                            popUpTo(SubGraph.Auth) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
        composable<SubGraph.Main> {
            MainScreen (
                onLogout = {
                    navController.navigate(SubGraph.Auth) {
                        // Clear the entire back stack
                        popUpTo(SubGraph.Main) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }


}