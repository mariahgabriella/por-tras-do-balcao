package com.example.portrasdobalcao.ui.navigation


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petshop.data.local.SessaoManager
import com.example.petshop.ui.features.login.LoginScreen
import com.example.petshop.ui.features.perfil.PerfilScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current,
) {
    val sessaoManager = SessaoManager(context)
    val startDestination = if (sessaoManager.estaLogado()) {
        NavTarget.Perfil.route
    } else {
        NavTarget.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavTarget.Login.route) {
            LoginScreen(
                onLoginSucesso = {
                    navController.navigate(NavTarget.Perfil.route) {
                        popUpTo(NavTarget.Login.route) { inclusive = true }
                    }
                },
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
