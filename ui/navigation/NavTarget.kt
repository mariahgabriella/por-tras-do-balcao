package com.example.portrasdobalcao.ui.navigation

sealed class NavTarget(val route: String) {
    data object Login : NavTarget("login")
    data object Perfil : NavTarget("perfil")
}
