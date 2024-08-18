package com.example.fungid.ui.pages.register

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.example.fungid.exceptions.network.ServerUnreacheableException
import com.example.fungid.exceptions.register.EmailTakenException
import com.example.fungid.exceptions.register.PasswordMismatchException
import com.example.fungid.exceptions.register.UncompletedFieldsException
import com.example.fungid.exceptions.register.UnspecifiedConflictException
import com.example.fungid.exceptions.register.UsernameTakenException
import com.example.fungid.util.CustomClickableText
import com.example.fungid.util.TAG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationPage(
    onChooseLogin: () -> Unit,
    onRegisterSuccessful: () -> Unit
) {

    val registerViewModel = viewModel<RegisterViewModel>(factory = RegisterViewModel.Factory)
    val registerUiState = registerViewModel.uiState

    val scrollState = rememberScrollState()

    var emailText by rememberSaveable { mutableStateOf("") }
    var usernameText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }
    var passwordConfirmationText by rememberSaveable {
        mutableStateOf("")
    }

    val errorColor = Color.Red
    val textFieldDefaultColors = OutlinedTextFieldDefaults.colors()
    val textFieldErrorColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = errorColor,
        unfocusedLabelColor = errorColor,
        focusedBorderColor = errorColor,
        focusedLabelColor = errorColor
    )

    var usernameTextFieldColors = textFieldDefaultColors
    var emailTextFieldColors = textFieldDefaultColors
    var passwordTextFieldColors = textFieldDefaultColors
    var passwordConfirmationTextFieldColors = textFieldDefaultColors

    val errorMessageText: String
    when (registerUiState.registrationError) {
        null -> {
            errorMessageText = ""
        }

        is PasswordMismatchException -> {
            errorMessageText = "The 2 passwords you entered do not match."
            passwordTextFieldColors = textFieldErrorColors
            passwordConfirmationTextFieldColors = textFieldErrorColors
        }

        is UsernameTakenException -> {
            errorMessageText = "The username is already taken"
            usernameTextFieldColors = textFieldErrorColors
        }

        is EmailTakenException -> {
            errorMessageText = "The email is already taken"
            emailTextFieldColors = textFieldErrorColors
        }

        is UnspecifiedConflictException -> errorMessageText =
            "The registration failed due to an unknown conflict between your data and another user's data."

        is ServerUnreacheableException -> errorMessageText = "The server could not be reached."
        is UncompletedFieldsException -> {
            errorMessageText = "The email, username and password fields must ALL be completed."
            usernameTextFieldColors = textFieldErrorColors
            passwordTextFieldColors = textFieldErrorColors
            passwordConfirmationTextFieldColors = textFieldErrorColors
            emailTextFieldColors = textFieldErrorColors
        }

        else -> errorMessageText = "The registration failed for an unknown reason."
    }


    LaunchedEffect(registerUiState.registrationCompleted) {
        Log.d(TAG, "Registration completed")
        if (registerUiState.registrationCompleted) {
            onRegisterSuccessful()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.register)) }) },
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(28.dp)
                .verticalScroll(scrollState),
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
            //Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),

                colors = emailTextFieldColors,
                singleLine = true,
                value = emailText,
                onValueChange = { emailText = it },
                label = { Text("Email") },
                placeholder = { Text("Type email here") },
                shape = RoundedCornerShape(percent = 20),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),

                colors = usernameTextFieldColors,
                singleLine = true,
                value = usernameText,
                onValueChange = { usernameText = it },
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

                colors = passwordTextFieldColors,
                singleLine = true,
                value = passwordText,
                onValueChange = { passwordText = it },
                label = { Text("Password") },
                placeholder = { Text("Type password here") },
                shape = RoundedCornerShape(percent = 20),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),

                colors = passwordConfirmationTextFieldColors,
                singleLine = true,
                value = passwordConfirmationText,
                onValueChange = { passwordConfirmationText = it },
                label = { Text("Confirm Password") },
                placeholder = { Text("Type password confirmation here") },
                shape = RoundedCornerShape(percent = 20),
                visualTransformation = PasswordVisualTransformation(),
                keyboardActions = KeyboardActions(
                    onDone = {
                        Log.d(TAG, "register...")
                        registerViewModel.register(
                            usernameText,
                            passwordText,
                            passwordConfirmationText,
                            emailText
                        )
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )
            Text(errorMessageText, color = errorColor)
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Button(
                onClick = {
                    Log.d(TAG, "register...")
                    registerViewModel.register(
                        usernameText,
                        passwordText,
                        passwordConfirmationText,
                        emailText
                    )
                },
                enabled = !registerUiState.isRegistering,
                shape = RoundedCornerShape(percent = 20),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                if (registerUiState.isRegistering) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(32.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                } else {
                    Text("Register!")
                }
            }

            CustomClickableText("Already have an account? ", "Log in!") {
                onChooseLogin()
            }
        }
    }

}