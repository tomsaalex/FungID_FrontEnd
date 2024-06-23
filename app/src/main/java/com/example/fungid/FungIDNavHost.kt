package com.example.fungid

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fungid.pages.classification_jobs.MainCameraPage
import com.example.fungid.pages.login.LoginPage
import com.example.fungid.pages.register.RegistrationPage
import com.example.fungid.pages.classification_jobs.ClassificationsPage
import kotlinx.coroutines.ExperimentalCoroutinesApi

val LOGIN_ROUTE = "login"
val REGISTRATION_ROUTE = "registration"
val CLASSIFICATION_JOB_LIST_ROUTE = "classification_jobs"
val CAMERA_PAGE = "camera_page"

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun FungIDNavHost() {
    val navController = rememberNavController()

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
                onChooseOffline = {
                    Log.d(
                        "LoginPage",
                        "Offline Mode Clicked"
                    )
                },
                onLoginSuccessful = {
                    navController.navigate(CLASSIFICATION_JOB_LIST_ROUTE)
                }
            )
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
                }
            )
        }
        composable(CLASSIFICATION_JOB_LIST_ROUTE) {
            ClassificationsPage(
                onActivateCamera = {
                    Log.d("ClassificationsPage", "Triggering camera")
                    navController.navigate(CAMERA_PAGE)
                }
            )
        }
        composable(CAMERA_PAGE) {
            //CameraCapture()
            MainCameraPage()
        }
    }
}