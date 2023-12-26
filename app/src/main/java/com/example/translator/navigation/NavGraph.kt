package com.example.translator.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.translator.ui.screens.camera.CameraViewModel
import com.example.translator.ui.screens.language.LanguageScreen
import com.example.translator.ui.screens.language.LanguageViewModel
import com.example.translator.ui.screens.main.MainScreen
import com.example.translator.ui.screens.realtime.RealtimeViewModel
import com.example.translator.ui.screens.text.TextViewModel

@Composable
fun SetUpNavigation(
    navController: NavHostController,
    context: Context,
    cameraViewModel: CameraViewModel,
    textViewModel: TextViewModel,
    languageViewModel: LanguageViewModel,
    realtimeViewModel: RealtimeViewModel
) {
    NavHost(
        navController = navController, route = Graph.Main.graph,
        startDestination = Screen.Main.route
    ) {
        composable(route = Screen.Main.route) {
            MainScreen(
                onCameraClick = { navController.navigate(Graph.Camera.graph) },
                onTextClick = { navController.navigate(Graph.Text.graph) },
                navigateToLanguage = { navController.navigate(Screen.Language.route) },
                navigateToRealTime = { navController.navigate(Graph.RealTime.graph) }
            )
        }

        composable(route = Screen.Language.route) {
            LanguageScreen(
                languageViewModel = languageViewModel,
                navController = navController,
            )
        }

        realTimeNavigation(
            navController = navController,
            realtimeViewModel = realtimeViewModel
        )

        cameraNavigation(
            context = context,
            navController = navController,
            cameraViewModel = cameraViewModel
        )

        textNavigation(
            navController = navController,
            textViewModel = textViewModel
        )
    }
}


sealed class Graph(val graph: String) {
    object Main : Graph(
        graph = "root_graph"
    )

    object Camera : Graph(
        graph = "camera_graph"
    )

    object Text : Graph(
        graph = "text_graph"
    )

    object RealTime: Graph(
        graph = "realtime_graph"
    )
}

sealed class Screen(val route: String) {
    object Main : Screen(
        route = "main"
    )

    object Language : Screen(
        route = "language"
    )

    object RealTime : Screen(
        route = "realtime"
    ){
        fun addArgument(argument: String): String {
            return RealTime.route + "/${argument}"
        }
    }

    object Camera : Screen(
        route = "camera"
    ) {
        fun addArgument(argument: String): String {
            return Camera.route + "/${argument}"
        }
    }

    object Text : Screen(
        route = "text"
    ) {
        fun addArgument(argument: String): String {
            return Text.route + "/${argument}"
        }
    }
}