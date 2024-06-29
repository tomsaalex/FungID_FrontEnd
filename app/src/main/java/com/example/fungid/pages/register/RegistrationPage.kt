package com.example.fungid.pages.register

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fungid.R
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

    var emailText by rememberSaveable { mutableStateOf("") }
    var usernameText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }

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
            //Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
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
                value = passwordText,
                onValueChange = { passwordText = it },
                label = { Text("Password") },
                placeholder = { Text("Type password here") },
                shape = RoundedCornerShape(percent = 20),
                visualTransformation = PasswordVisualTransformation(),
                keyboardActions = KeyboardActions(
                    onDone = {
                        Log.d(TAG, "register...")
                        registerViewModel.register(usernameText, passwordText, emailText)
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))

            Button(
                onClick = {
                    Log.d(TAG, "register...")
                    registerViewModel.register(usernameText, passwordText, emailText)
                },
                shape = RoundedCornerShape(percent = 20),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register!")
            }


            CustomClickableText("Already have an account? ", "Log in!") {
                onChooseLogin()
            }
        }
    }

}