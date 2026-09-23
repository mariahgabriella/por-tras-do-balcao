package com.example.petshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.petshop.ui.navigation.NavGraph
import com.example.petshop.ui.theme.PetshopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetshopTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavGraph()
                }
            }
        }
    }
}