package com.example.portrasdobalcao.ui.navigation

sealed class NavTarget(val route: String) {
    data object Splash : NavTarget("splash")
    data object About : NavTarget("about")
    data object Home : NavTarget("home")
    data object Login : NavTarget("login")
    data object Perfil : NavTarget("perfil")
    data object Estoque : NavTarget("estoque")
}