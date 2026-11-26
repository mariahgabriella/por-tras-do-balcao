package com.example.portrasdobalcao.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta oficial do Atrás do Balcão: ameixa (estrutura), amarelo (destaque) e creme (fundo).
val BalcaoPlum = Color(0xFF412234)
val BalcaoYellow = Color(0xFFF0C808)
val BalcaoCream = Color(0xFFFFF1D0)

// Tons auxiliares derivados das três cores acima, sem criar novas cores de marca.
val BalcaoMutedText = BalcaoPlum.copy(alpha = 0.78f)
val BalcaoLightSurface = BalcaoYellow.copy(alpha = 0.14f)
val BalcaoLightOutline = BalcaoPlum.copy(alpha = 0.18f)
val BalcaoDarkSurface = BalcaoCream.copy(alpha = 0.10f)
val BalcaoDarkOutline = BalcaoCream.copy(alpha = 0.22f)
val BalcaoDarkMutedText = BalcaoCream.copy(alpha = 0.78f)

// Verde de sucesso: cor funcional, não é cor de marca.
val SuccessGreen = Color(0xFF4CAF50)
