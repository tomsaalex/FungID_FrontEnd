package com.example.fungid.core.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.auth0.android.jwt.JWT
import com.example.fungid.core.data.UserPreferences
import com.example.fungid.core.data.remote.Api
import com.example.fungid.ui.pages.classification_jobs.classification_page.MushroomClassificationPage
import com.example.fungid.ui.pages.classification_jobs.classifications_page.ClassificationsPage
import com.example.fungid.ui.pages.classification_jobs.main_camera_page.MainCameraPage
import com.example.fungid.ui.pages.login.LoginPage
import com.example.fungid.ui.pages.register.RegistrationPage
import kotlinx.coroutines.ExperimentalCoroutinesApi

val LOGIN_ROUTE = "login"
val REGISTRATION_ROUTE = "registration"
val CLASSIFICATION_JOB_LIST_ROUTE = "classification_jobs"
val CAMERA_PAGE = "camera_page"

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun FungIDNavHost() {
    val navController = rememberNavController()

    val userPreferencesViewModel =
        viewModel<UserPreferencesViewModel>(factory = UserPreferencesViewModel.Factory)
    val userPreferencesUiState by userPreferencesViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = UserPreferences()
    )

    val fungIDViewModel = viewModel<FungIDViewModel>(factory = FungIDViewModel.Factory)

    NavHost(
        navController = navController,
        startDestination = LOGIN_ROUTE
    ) {
        composable(LOGIN_ROUTE) {
            LoginPage(
                onChooseRegister = {
                    Log.d(
                        "LoginPage",
                        "Register Account Clicked"
                    )
                    navController.navigate(REGISTRATION_ROUTE)
                },
                onLoginSuccessful = {
                    navController.navigate(CLASSIFICATION_JOB_LIST_ROUTE)
                })
        }
        composable(REGISTRATION_ROUTE) {
            RegistrationPage(
                onChooseLogin = {
                    Log.d(
                        "RegistrationPage",
                        "Login Clicked"
                    )
                    navController.navigate(LOGIN_ROUTE)
                },
                onRegisterSuccessful = {
                    navController.navigate(CLASSIFICATION_JOB_LIST_ROUTE)
                })
        }
        composable(CLASSIFICATION_JOB_LIST_ROUTE) {
            ClassificationsPage(
                onActivateCamera = {
                    Log.d("ClassificationsPage", "Triggering camera")
                    navController.navigate(CAMERA_PAGE)
                },
                onMushroomInstanceClick = { mushroomInstanceId ->
                    Log.d("ClassificationsPage", "navigate to mushroomInstance $mushroomInstanceId")
                    navController.navigate("$CLASSIFICATION_JOB_LIST_ROUTE/$mushroomInstanceId")
                },
                onLogout = {

                    Log.d("ClassificationsPage", "Log out initiated")
                    fungIDViewModel.logout()
                    Api.tokenInterceptor.token = null
                    navController.navigate(LOGIN_ROUTE) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(CAMERA_PAGE) {
            MainCameraPage(
                redirectToSubmittedImagePage = { mushroomInstanceId ->
                    Log.d("MainCameraPage", "navigate to mushroomInstance $mushroomInstanceId")
                    navController.navigate("$CLASSIFICATION_JOB_LIST_ROUTE/$mushroomInstanceId") {
                        popUpTo(CLASSIFICATION_JOB_LIST_ROUTE)
                    }
                }
            )
        }
        composable(
            route = "$CLASSIFICATION_JOB_LIST_ROUTE/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) {
            MushroomClassificationPage(mushroomInstanceId = it.arguments?.getString("id"))
        }
    }

    LaunchedEffect(userPreferencesUiState.token) {
        if (userPreferencesUiState.token.isNotEmpty()) {
            Log.d("FungIDNavHost", "Launched effect skip login")
            val token = JWT(userPreferencesUiState.token)
            if(token.isExpired(0)) {
                Log.d("FungIDNavHost", "Token is expired")
                return@LaunchedEffect
            }
            Api.tokenInterceptor.token = userPreferencesUiState.token

            navController.navigate(CLASSIFICATION_JOB_LIST_ROUTE) {
                popUpTo(0)
            }
        }
    }
}