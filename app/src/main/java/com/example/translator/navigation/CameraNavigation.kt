package com.example.translator.navigation

import android.content.Context
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.translator.ui.screens.camera.CameraScreen
import com.example.translator.ui.screens.camera.CameraTranslatorScreen
import com.example.translator.ui.screens.camera.CameraViewModel
import com.example.translator.ui.screens.camera.CaptureScreen

fun NavGraphBuilder.cameraNavigation(
    context: Context,
    navController: NavHostController,
    cameraViewModel: CameraViewModel
) {
    navigation(
        route = Graph.Camera.graph,
        startDestination = Screen.Camera.route
    ) {
        composable(route = Screen.Camera.route) {
            CameraScreen(
                context = context,
                navController = navController,
                cameraViewModel = cameraViewModel
            )
        }

        composable(route = Screen.Camera.addArgument("capture")) {
            CaptureScreen(
                context = context,
                navController = navController,
                onTakePhoto = { cameraViewModel.assignPhoto(it) }
            )
        }

        composable(route = Screen.Camera.addArgument("translator")) {
            CameraTranslatorScreen(
                cameraViewModel = cameraViewModel,
                onBackClicked = { navController.popBackStack() },
                context = context
            )
        }
    }
}