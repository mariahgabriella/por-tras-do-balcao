package com.example.portrasdobalcao.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.portrasdobalcao.data.local.SessaoManager
import com.example.portrasdobalcao.ui.features.about.AboutScreen
import com.example.portrasdobalcao.ui.features.login.LoginScreen
import com.example.portrasdobalcao.ui.features.perfil.PerfilScreen
import com.example.portrasdobalcao.ui.features.splash.SplashScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current,
) {
    val sessaoManager = remember { SessaoManager(context) }

    NavHost(
        navController = navController,
        startDestination = NavTarget.Splash.route
    ) {
        composable(NavTarget.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    val destino = if (sessaoManager.estaLogado()) {
                        NavTarget.Perfil.route
                    } else {
                        NavTarget.About.route
                    }
                    navController.navigate(destino) {
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

        composable(NavTarget.Login.route) {
            LoginScreen(
                onLoginSucesso = {
                    navController.navigate(NavTarget.Perfil.route) {
                        popUpTo(NavTarget.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavTarget.Perfil.route) {
            PerfilScreen(
                onSair = {
                    navController.navigate(NavTarget.Login.route) {
                        popUpTo(NavTarget.Perfil.route) { inclusive = true }
                    }
                }
            )
        }
    }
}