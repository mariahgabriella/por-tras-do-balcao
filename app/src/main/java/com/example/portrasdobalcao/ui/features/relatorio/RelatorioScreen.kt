package com.example.portrasdobalcao.ui.features.relatorio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.portrasdobalcao.model.ProdutoItem
import com.example.portrasdobalcao.ui.components.BarraTopo
import com.example.portrasdobalcao.ui.components.CartaoApp
import com.example.portrasdobalcao.ui.theme.PorTrasDoBalcaoTheme
import com.example.portrasdobalcao.util.formatarMoeda

@Composable
fun RelatorioScreen(
    estado: RelatorioUiState,
    onVoltar: () -> Unit,
    onTentarDeNovo: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            // Como no print, a seta de voltar fica à direita do título.
            BarraTopo(
                titulo = stringResource(R.string.relatorio_titulo),
                acoes = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.comum_voltar),
                        )
                    }
                },
            )
        },
    ) { padding ->
        when {
            estado.carregando -> Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            estado.erro -> Column(
                Modifier.padding(padding).fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(stringResource(R.string.comum_erro_carregar), textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onTentarDeNovo) { Text(stringResource(R.string.comum_tentar_de_novo)) }
            }

            else -> LazyColumn(
                Modifier.padding(padding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item { CartaoTotal(estado.totalInvestido) }
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CartaoNumero(
                            rotulo = stringResource(R.string.relatorio_produtos),
                            valor = estado.quantidadeProdutos.toString(),
                            modifier = Modifier.weight(1f),
                        )
                        CartaoNumero(
                            rotulo = stringResource(R.string.relatorio_itens),
                            valor = estado.totalItens.toString(),
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                item {
                    Text(
                        stringResource(R.string.relatorio_por_valor),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
                if (estado.produtos.isEmpty()) {
                    item {
                        Text(
                            stringResource(R.string.relatorio_vazio),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    items(estado.produtos, key = { it.id }) { ItemRelatorio(it) }
                }
            }
        }
    }
}

@Composable
private fun CartaoTotal(total: Double) {
    CartaoApp(
        Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.primary,
    ) {
        Column(Modifier.padding(horizontal = 24.dp, vertical = 20.dp)) {
            Text(
                stringResource(R.string.relatorio_total_investido),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                formatarMoeda(total),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
private fun CartaoNumero(rotulo: String, valor: String, modifier: Modifier = Modifier) {
    CartaoApp(modifier) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                rotulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(valor, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun ItemRelatorio(item: ProdutoItem) {
    CartaoApp(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(item.nome, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    stringResource(R.string.relatorio_item_detalhe, item.quantidade, formatarMoeda(item.precoCusto)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(formatarMoeda(item.subtotal), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

private val estadoPreview = RelatorioUiState(
    carregando = false,
    totalInvestido = 35.0,
    quantidadeProdutos = 2,
    totalItens = 5,
    produtos = listOf(
        ProdutoItem(1, "Caderno", "Papelaria", 2, 1, 10.0),
        ProdutoItem(2, "Caneta", "Papelaria", 3, 1, 5.0),
    ),
)

@Preview(name = "Relatório claro", showSystemUi = true)
@Composable
private fun RelatorioClaroPreview() {
    PorTrasDoBalcaoTheme(darkTheme = false) { RelatorioScreen(estadoPreview, {}, {}) }
}

@Preview(name = "Relatório escuro", showSystemUi = true)
@Composable
private fun RelatorioEscuroPreview() {
    PorTrasDoBalcaoTheme(darkTheme = true) { RelatorioScreen(estadoPreview, {}, {}) }
}
