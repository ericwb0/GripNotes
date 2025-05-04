package com.example.gripnotes.view.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gripnotes.R
import com.example.gripnotes.viewmodel.LoginViewModel

/**
 * Login screen composable.
 *
 * @param onLogin Callback function to be called when login is successful. Meant for navigation.
 * @author ericwb0
 */
@Composable
fun LoginScreen(onLogin: () -> Unit, onSignUp: () -> Unit) {
    val loginViewModel: LoginViewModel = viewModel()
    val context = LocalContext.current // For Toast messages

    // State variables for username and password
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // State from ViewModel
    val error by loginViewModel.error.collectAsStateWithLifecycle("")
    val isLoading by loginViewModel.isLoading.collectAsStateWithLifecycle(false)
    val isReady by loginViewModel.isReady.collectAsStateWithLifecycle(false)

    // If the user is already logged in, navigate to the main screen

    LaunchedEffect(isReady) {
        // ViewModel signals that the user is logged in
        if(isReady) {
            username = ""
            password = ""
            Toast.makeText(
                context,
                "Logged in successfully",
                Toast.LENGTH_SHORT
            ).show()
            onLogin()
        }
    }
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp)
            .testTag("loginScreen")
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Login",
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
            modifier = Modifier.testTag("loginButton"),
            onClick = {
                loginViewModel.logIn(username, password)
            },
            enabled = !isLoading,
        ) {
            if(isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(text = "Login")
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        TextButton(
            modifier = Modifier.fillMaxWidth().testTag("signUpInsteadButton"),
            onClick = {
                onSignUp()
            },
            enabled = !isLoading
        ) {
            Text(
                text = stringResource(R.string.sign_up_instead),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}