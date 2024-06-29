package com.example.fungid.pages.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fungid.R
import com.example.fungid.exceptions.login.InvalidCredentialsException
import com.example.fungid.exceptions.network.ServerUnreacheableException
import com.example.fungid.util.CustomClickableText
import com.example.fungid.util.TAG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    onChooseRegister: () -> Unit,
    onChooseOffline: () -> Unit,
    onLoginSuccessful: () -> Unit
) {

    val loginViewModel = viewModel<LoginViewModel>(factory = LoginViewModel.Factory)
    val loginUiState = loginViewModel.uiState

    var usernameText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }

    val errorColor = Color.Red

    val outLinedTextFieldColors = if (loginUiState.authenticatingError != null) {
        OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = errorColor,
            unfocusedLabelColor = errorColor,
            focusedBorderColor = errorColor,
            focusedLabelColor = errorColor
        )
    } else {
        OutlinedTextFieldDefaults.colors()
    }

    val errorMessageText: String =
        when (loginUiState.authenticatingError) {
            null -> ""
            is ServerUnreacheableException -> "The server could not be reached."
            is InvalidCredentialsException -> "The credentials provided are invalid."
            else -> "The authentication failed for an unknown reason."
        }

    LaunchedEffect(loginUiState.authenticationCompleted) {
        Log.d(TAG, "Auth completed")
        if (loginUiState.authenticationCompleted) {
            onLoginSuccessful()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.login)) }) },
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Image(
                painter = painterResource(R.drawable.ic_fung_id_login_logo_cropped),
                contentDescription = "Application Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),

                colors = outLinedTextFieldColors,
                singleLine = true,
                value = usernameText,
                onValueChange = { newUsername -> usernameText = newUsername },
                label = { Text("Username") },
                placeholder = { Text("Type username here") },
                shape = RoundedCornerShape(percent = 20),

                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),

                colors = outLinedTextFieldColors,
                value = passwordText,
                onValueChange = { newPassword -> passwordText = newPassword },
                label = { Text("Password") },
                placeholder = { Text("Type password here") },
                shape = RoundedCornerShape(percent = 20),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        Log.d(TAG, "login...")
                        loginViewModel.login(usernameText, passwordText)
                    }
                )
            )

            Text(errorMessageText, color = errorColor)

            Spacer(modifier = Modifier.fillMaxHeight(0.1f))

            Button(
                onClick = {
                    Log.d(TAG, "login...")
                    loginViewModel.login(usernameText, passwordText)
                },
                shape = RoundedCornerShape(percent = 20),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log In!")
            }


            CustomClickableText("Don't have an account yet? ", "Make one!") {
                onChooseRegister()
            }
            CustomClickableText("Want to use FungID offline? ", "Click here!") {
                onChooseOffline()
            }
        }
    }

}