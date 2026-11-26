package com.example.portrasdobalcao.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Esquema escuro da marca: fundo ameixa, texto creme e destaques amarelos.
private val DarkColorScheme = darkColorScheme(
    primary = BalcaoYellow,
    onPrimary = BalcaoPlum,
    primaryContainer = BalcaoDarkSurface,
    onPrimaryContainer = BalcaoCream,
    secondary = BalcaoCream,
    onSecondary = BalcaoPlum,
    secondaryContainer = BalcaoDarkSurface,
    onSecondaryContainer = BalcaoCream,
    tertiary = BalcaoYellow,
    onTertiary = BalcaoPlum,
    tertiaryContainer = BalcaoDarkSurface,
    onTertiaryContainer = BalcaoYellow,
    background = BalcaoPlum,
    onBackground = BalcaoCream,
    surface = BalcaoPlum,
    onSurface = BalcaoCream,
    surfaceVariant = BalcaoDarkSurface,
    onSurfaceVariant = BalcaoDarkMutedText,
    outlineVariant = BalcaoDarkOutline
)

// Esquema claro da marca: fundo creme, texto ameixa e destaques amarelos.
private val LightColorScheme = lightColorScheme(
    primary = BalcaoPlum,
    onPrimary = BalcaoCream,
    primaryContainer = BalcaoYellow,
    onPrimaryContainer = BalcaoPlum,
    secondary = BalcaoYellow,
    onSecondary = BalcaoPlum,
    secondaryContainer = BalcaoLightSurface,
    onSecondaryContainer = BalcaoPlum,
    tertiary = BalcaoYellow,
    onTertiary = BalcaoPlum,
    tertiaryContainer = BalcaoYellow,
    onTertiaryContainer = BalcaoPlum,
    background = BalcaoCream,
    onBackground = BalcaoPlum,
    surface = BalcaoCream,
    onSurface = BalcaoPlum,
    surfaceVariant = BalcaoLightSurface,
    onSurfaceVariant = BalcaoMutedText,
    outlineVariant = BalcaoLightOutline
)

// Tema do app. dynamicColor fica desligado por padrão para que as cores do papel de parede
// não substituam a identidade visual. Ele só deve ser ligado em testes ou comparações.
@Composable
fun PorTrasDoBalcaoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
