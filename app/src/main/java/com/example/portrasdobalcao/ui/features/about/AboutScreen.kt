package com.example.portrasdobalcao.ui.features.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.portrasdobalcao.R
import com.example.portrasdobalcao.ui.components.BarraTopo
import com.example.portrasdobalcao.ui.components.CartaoApp
import com.example.portrasdobalcao.ui.theme.PorTrasDoBalcaoTheme

@Composable
fun AboutScreen(
    versao: String,
    integrantes: List<String>,
    onVoltar: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { BarraTopo(titulo = stringResource(R.string.sobre_titulo), onVoltar = onVoltar) },
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            com.example.portrasdobalcao.ui.components.LogoApp()
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
            )
            Text(
                stringResource(R.string.sobre_versao, versao),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(24.dp))

            CartaoApp(Modifier.fillMaxWidth()) {
                Text(
                    stringResource(R.string.sobre_descricao),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Spacer(Modifier.height(16.dp))

            CartaoApp(Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(
                        stringResource(R.string.sobre_integrantes_titulo),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(8.dp))
                    integrantes.forEach { nome ->
                        Text(
                            nome,
                            modifier = Modifier.padding(vertical = 2.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SobrePreviewConteudo() {
    AboutScreen(versao = "1.0", integrantes = listOf("Integrante 1", "Integrante 2", "Integrante 3"), onVoltar = {})
}

@Preview(name = "Sobre claro", showSystemUi = true)
@Composable
private fun SobreClaroPreview() {
    PorTrasDoBalcaoTheme(darkTheme = false) { SobrePreviewConteudo() }
}

@Preview(name = "Sobre escuro", showSystemUi = true)
@Composable
private fun SobreEscuroPreview() {
    PorTrasDoBalcaoTheme(darkTheme = true) { SobrePreviewConteudo() }
}
