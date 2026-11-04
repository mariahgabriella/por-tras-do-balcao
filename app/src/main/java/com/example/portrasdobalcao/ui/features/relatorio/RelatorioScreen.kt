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
import com.example.portrasdobalcao.ui.UiState
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
        // O que faz: desenha o relatório de acordo com o UiState atual, calculando os totais na hora.
        // Por que existe: evita que a tela leia campos de totais que podiam ficar desatualizados em relação à lista.
        // O que quebra se sumir: a tela volta a ler totais prontos do estado, sem garantia de que batem com a lista.
        when (val atual = estado.tela) {
            is UiState.Carregando -> Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            is UiState.Erro -> Column(
                Modifier.padding(padding).fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(atual.mensagem, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onTentarDeNovo) { Text(stringResource(R.string.comum_tentar_de_novo)) }
            }

            is UiState.Vazio -> Column(
                Modifier.padding(padding).fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(stringResource(R.string.relatorio_vazio), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            is UiState.Sucesso -> {
                val totalInvestido = atual.itens.sumOf { it.subtotal }
                val quantidadeProdutos = atual.itens.size
                val totalItens = atual.itens.sumOf { it.quantidade }

                LazyColumn(
                    Modifier.padding(padding),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item { CartaoTotal(totalInvestido) }
                    item {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            CartaoNumero(
                                rotulo = stringResource(R.string.relatorio_produtos),
                                valor = quantidadeProdutos.toString(),
                                modifier = Modifier.weight(1f),
                            )
                            CartaoNumero(
                                rotulo = stringResource(R.string.relatorio_itens),
                                valor = totalItens.toString(),
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
                    items(atual.itens, key = { it.id }) { ItemRelatorio(it) }
                }
            }
        }
    }
}

@Composable
private fun CartaoTotal(total: Double) {
    CartaoApp(Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth().padding(20.dp)) {
            Text(
                stringResource(R.string.relatorio_total_investido),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                formatarMoeda(total),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun CartaoNumero(rotulo: String, valor: String, modifier: Modifier = Modifier) {
    CartaoApp(modifier) {
        Column(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(valor, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(rotulo, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ItemRelatorio(item: ProdutoItem) {
    CartaoApp(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.Start.let { Alignment.CenterHorizontally },
        ) {
            Column(Modifier.weight(1f)) {
                Text(item.nome, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(
                    "${item.quantidade} unid.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(formatarMoeda(item.subtotal), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        }


    }
}
