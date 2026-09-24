package com.example.petshop.ui.navigation

sealed class NavTarget(val route: String) {
    data object Home : NavTarget("home")
}
