package com.example.gripnotes.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gripnotes.R
import com.example.gripnotes.viewmodel.SignUpViewModel

/**
 * Sign Up screen composable.
 *
 * @param onSignUp Callback function to be called when sign up is successful. Meant for navigation.
 * @param onLogin Callback function to be called when the user wants to log in instead. Meant for navigation.
 * @author ericwb0
 */
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = viewModel(),
    onSignUp : () -> Unit,
    onLogin: () -> Unit
) {

    // State variables for username and password
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // State from ViewModel
    val error by viewModel.error.collectAsStateWithLifecycle("")
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)
    val isReady by viewModel.isReady.collectAsStateWithLifecycle(false)

    // If the user is already logged in, navigate to the main screen

    LaunchedEffect(isReady) {
        if(isReady) {
            // ViewModel signals that the user signed up successfully
            username = ""
            password = ""
            confirmPassword = ""
            onSignUp()
        }
    }
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(16.dp)
                .testTag("signUpScreen")
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth()
                    .testTag("usernameField"),
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "Username") },
                isError = error.isNotEmpty(),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth()
                    .testTag("passwordField"),
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                isError = error.isNotEmpty(),
                visualTransformation = PasswordVisualTransformation(),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth()
                    .testTag("confirmPasswordField"),
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(text = "Confirm Password") },
                isError = error.isNotEmpty(),
                visualTransformation = PasswordVisualTransformation(),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (error.isNotEmpty()) {
                Text(
                    modifier = Modifier.testTag("errorText"),
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("SignUpButton"),
                onClick = {
                    viewModel.signUp(username, password, confirmPassword)
                },
                enabled = !isLoading,
            ) {
                if(isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(text = "Sign Up")
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            TextButton(
                modifier = Modifier.fillMaxWidth().testTag("logInInsteadButton"),
                onClick = {
                    onLogin()
                },
                enabled = !isLoading
            ) {
                Text(
                    text = stringResource(R.string.log_in_instead),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
    }


    }
}