package com.example.portrasdobalcao.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.portrasdobalcao.ui.features.about.AboutScreen
import com.example.portrasdobalcao.ui.features.splash.SplashScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NavTarget.Splash.route
    ) {
        composable(NavTarget.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(NavTarget.About.route) {
                        popUpTo(NavTarget.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavTarget.About.route) {
            AboutScreen()
        }

        composable(NavTarget.Home.route) {
            AboutScreen()
        }
    }
}
