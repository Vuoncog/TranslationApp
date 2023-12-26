package com.example.translator.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.translator.ui.screens.text.TextScreen
import com.example.translator.ui.screens.text.TextViewModel

fun NavGraphBuilder.textNavigation(
    navController: NavHostController,
    textViewModel: TextViewModel
) {
    navigation(
        route = Graph.Text.graph,
        startDestination = Screen.Text.route
    ) {
        composable(route = Screen.Text.route) {
            TextScreen(
                navController = navController,
                textViewModel = textViewModel
            )
        }
    }
}