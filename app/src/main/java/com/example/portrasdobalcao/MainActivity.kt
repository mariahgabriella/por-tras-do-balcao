package com.example.portrasdobalcao

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.portrasdobalcao.data.local.TemaApp
import com.example.portrasdobalcao.ui.navigation.NavGraph
import com.example.portrasdobalcao.ui.theme.PorTrasDoBalcaoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = AppContainer.getInstance(applicationContext)
        setContent {
            // Tema escolhido na tela de Perfil (Claro / Escuro / Do Sistema).
            val tema by container.temaPreferences.tema.collectAsState()
            val escuro = when (tema) {
                TemaApp.CLARO -> false
                TemaApp.ESCURO -> true
                TemaApp.SISTEMA -> isSystemInDarkTheme()
            }
            PorTrasDoBalcaoTheme(darkTheme = escuro) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavGraph(container = container)
                }
            }
        }
    }
}
