package com.example.translator.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.translator.ui.screens.realtime.RealTimeScreen
import com.example.translator.ui.screens.realtime.RealtimeViewModel
import com.example.translator.ui.screens.text.TextViewModel

fun NavGraphBuilder.realTimeNavigation(
    navController: NavHostController,
    realtimeViewModel: RealtimeViewModel
) {
    navigation(
        route = Graph.RealTime.graph,
        startDestination = Screen.RealTime.route
    ) {
        composable(
            route = Screen.RealTime.route
        ){
            RealTimeScreen(
                realtimeViewModel= realtimeViewModel
            )
        }
    }
}