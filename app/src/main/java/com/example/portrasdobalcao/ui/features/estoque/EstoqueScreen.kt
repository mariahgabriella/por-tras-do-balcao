package com.example.portrasdobalcao.ui.features.estoque

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.portrasdobalcao.R
import com.example.portrasdobalcao.model.ProdutoItem
import com.example.portrasdobalcao.model.ResultadoOperacao
import com.example.portrasdobalcao.model.TipoMovimentacao
import com.example.portrasdobalcao.ui.components.BarraTopo
import com.example.portrasdobalcao.ui.components.CampoComRotulo
import com.example.portrasdobalcao.ui.components.CartaoApp
import com.example.portrasdobalcao.ui.components.SeletorSegmentado
import com.example.portrasdobalcao.ui.components.SeloEstoqueBaixo
import com.example.portrasdobalcao.ui.theme.PorTrasDoBalcaoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstoqueScreen(
    estado: EstoqueUiState,
    onFiltroBaixoChange: (Boolean) -> Unit,
    onItemClick: (ProdutoItem) -> Unit,
    onFecharMovimentacao: () -> Unit,
    onTipoChange: (TipoMovimentacao) -> Unit,
    onQuantidadeChange: (String) -> Unit,
    onConfirmar: () -> Unit,
    onResultadoConsumido: () -> Unit,
    onTentarDeNovo: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val contexto = LocalContext.current

    LaunchedEffect(estado.resultado) {
        val resultado = estado.resultado ?: return@LaunchedEffect
        val texto = when (resultado) {
            ResultadoOperacao.Sucesso -> contexto.getString(R.string.estoque_sucesso)
            ResultadoOperacao.QuantidadeInvalida -> contexto.getString(R.string.estoque_qtd_invalida)
            is ResultadoOperacao.EstoqueInsuficiente -> contexto.getString(
                if (resultado.atual == 1) R.string.estoque_insuficiente_um else R.string.estoque_insuficiente_outros,
                resultado.atual,
            )
            ResultadoOperacao.Erro -> contexto.getString(R.string.estoque_erro)
        }
        snackbarHostState.showSnackbar(texto)
        onResultadoConsumido()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { BarraTopo(titulo = stringResource(R.string.estoque_titulo)) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(Modifier.padding(padding).padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(12.dp))
            FilterChip(
                selected = estado.somenteBaixo,
                onClick = { onFiltroBaixoChange(!estado.somenteBaixo) },
                label = { Text(stringResource(R.string.estoque_filtro_baixo)) },
            )
            Spacer(Modifier.height(12.dp))

            Box(Modifier.weight(1f).fillMaxWidth()) {
                when {
                    estado.carregando -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                    estado.erro -> Column(
                        Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(stringResource(R.string.comum_erro_carregar), textAlign = TextAlign.Center)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = onTentarDeNovo) { Text(stringResource(R.string.comum_tentar_de_novo)) }
                    }

                    estado.itens.isEmpty() -> Text(
                        text = stringResource(
                            if (estado.totalCadastrados == 0) R.string.estoque_vazio else R.string.estoque_vazio_filtro
                        ),
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )

                    else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(estado.itens, key = { it.id }) { item ->
                            ItemEstoque(item, onClick = { onItemClick(item) })
                        }
                    }
                }
            }
        }
    }

    estado.itemSelecionado?.let { item ->
        ModalBottomSheet(
            onDismissRequest = onFecharMovimentacao,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            Column(
                Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 16.dp)
                    .navigationBarsPadding(),
            ) {
                Text(item.nome, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                SeletorSegmentado(
                    opcoes = listOf(
                        TipoMovimentacao.ENTRADA to stringResource(R.string.estoque_entrada),
                        TipoMovimentacao.SAIDA to stringResource(R.string.estoque_saida),
                    ),
                    selecionado = estado.tipo,
                    onSelecionar = onTipoChange,
                )
                Spacer(Modifier.height(16.dp))
                CampoComRotulo(
                    rotulo = stringResource(R.string.estoque_campo_quantidade),
                    valor = estado.quantidadeTexto,
                    onValorChange = onQuantidadeChange,
                    teclado = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = !estado.processando,
                )
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Saldo(stringResource(R.string.estoque_saldo_antes), item.quantidade.toString(), Modifier.weight(1f))
                    Saldo(
                        stringResource(R.string.estoque_saldo_depois),
                        estado.saldoDepois?.toString() ?: "—",
                        Modifier.weight(1f),
                    )
                }
                Spacer(Modifier.height(20.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = onFecharMovimentacao,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) { Text(stringResource(R.string.comum_cancelar)) }
                    Button(
                        onClick = onConfirmar,
                        enabled = !estado.processando,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) { Text(stringResource(R.string.estoque_confirmar), fontWeight = FontWeight.Bold) }
                }
                // O sheet cobre o Snackbar da tela; este repete a mensagem aqui dentro.
                SnackbarHost(snackbarHostState)
            }
        }
    }
}

@Composable
private fun Saldo(rotulo: String, valor: String, modifier: Modifier = Modifier) {
    CartaoApp(modifier) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Text(
                rotulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(valor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ItemEstoque(item: ProdutoItem, onClick: () -> Unit) {
    val corAlerta = if (item.estoqueBaixo) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
    CartaoApp(Modifier.fillMaxWidth(), onClick = onClick) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    item.nome,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                if (item.estoqueBaixo) SeloEstoqueBaixo()
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.estoque_atual, item.quantidade) + "  ·  " +
                    stringResource(R.string.estoque_minima, item.quantidadeMinima),
                style = MaterialTheme.typography.bodyMedium,
                color = if (item.estoqueBaixo) corAlerta else MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { item.progressoEstoque },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50)),
                color = corAlerta,
                trackColor = MaterialTheme.colorScheme.outlineVariant,
            )
        }
    }
}

private val estadoPreview = EstoqueUiState(
    carregando = false,
    totalCadastrados = 2,
    itens = listOf(
        ProdutoItem(1, "Caderno 96 folhas", "Papelaria", 2, 5, 12.5),
        ProdutoItem(2, "Caneta azul", "Papelaria", 40, 10, 1.2),
    ),
)

@Composable
private fun EstoquePreviewConteudo() {
    EstoqueScreen(
        estado = estadoPreview,
        onFiltroBaixoChange = {}, onItemClick = {}, onFecharMovimentacao = {},
        onTipoChange = {}, onQuantidadeChange = {}, onConfirmar = {},
        onResultadoConsumido = {}, onTentarDeNovo = {},
    )
}

@Preview(name = "Estoque claro", showSystemUi = true)
@Composable
private fun EstoqueClaroPreview() {
    PorTrasDoBalcaoTheme(darkTheme = false) { EstoquePreviewConteudo() }
}

@Preview(name = "Estoque escuro", showSystemUi = true)
@Composable
private fun EstoqueEscuroPreview() {
    PorTrasDoBalcaoTheme(darkTheme = true) { EstoquePreviewConteudo() }
}
