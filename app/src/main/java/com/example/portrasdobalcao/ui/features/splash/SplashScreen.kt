package com.example.portrasdobalcao.ui.features.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.portrasdobalcao.R
import com.example.portrasdobalcao.ui.components.LogoApp
import com.example.portrasdobalcao.ui.theme.PorTrasDoBalcaoTheme
import kotlinx.coroutines.delay

private const val DURACAO_SPLASH_MS = 1800L

@Composable
fun SplashScreen(
    logado: Boolean,
    onIrParaPrincipal: () -> Unit,
    onIrParaLogin: () -> Unit,
) {
    val irParaPrincipal by rememberUpdatedState(onIrParaPrincipal)
    val irParaLogin by rememberUpdatedState(onIrParaLogin)

    var visivel by remember { mutableStateOf(false) }
    val alfa by animateFloatAsState(
        targetValue = if (visivel) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "splashAlfa",
    )

    LaunchedEffect(Unit) {
        visivel = true
        delay(DURACAO_SPLASH_MS)
        if (logado) irParaPrincipal() else irParaLogin()
    }

    val corTexto = MaterialTheme.colorScheme.onPrimary

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            Modifier
                .alpha(alfa)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            LogoApp(
                tamanho = 96.dp,
                fundo = corTexto.copy(alpha = 0.15f),
                conteudo = corTexto,
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = corTexto,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.splash_slogan),
                style = MaterialTheme.typography.bodyLarge,
                color = corTexto,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(name = "Splash claro", showSystemUi = true)
@Composable
private fun SplashClaroPreview() {
    PorTrasDoBalcaoTheme(darkTheme = false) { SplashScreen(logado = false, onIrParaPrincipal = {}, onIrParaLogin = {}) }
}

@Preview(name = "Splash escuro", showSystemUi = true)
@Composable
private fun SplashEscuroPreview() {
    PorTrasDoBalcaoTheme(darkTheme = true) { SplashScreen(logado = false, onIrParaPrincipal = {}, onIrParaLogin = {}) }
}
