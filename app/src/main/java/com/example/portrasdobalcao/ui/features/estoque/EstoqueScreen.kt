package com.example.portrasdobalcao.ui.features.estoque

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.portrasdobalcao.R
import com.example.portrasdobalcao.model.ProdutoItem
import com.example.portrasdobalcao.model.ResultadoOperacao
import com.example.portrasdobalcao.model.TipoMovimentacao

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
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(estado.resultado) {
        val res = estado.resultado ?: return@LaunchedEffect
        val msg = when (res) {
            ResultadoOperacao.Sucesso -> context.getString(R.string.movimentacao_sucesso)
            ResultadoOperacao.QuantidadeInvalida -> context.getString(R.string.quantidade_invalida)
            is ResultadoOperacao.EstoqueInsuficiente -> context.getString(R.string.erro_estoque_insuficiente)
            ResultadoOperacao.Erro -> "Erro ao realizar operação"
        }
        snackbarHostState.showSnackbar(msg)
        onResultadoConsumido()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.titulo_estoque)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = estado.filtroApenasBaixo,
                    onCheckedChange = onFiltroBaixoChange
                )
                Text("Ver apenas estoque baixo", modifier = Modifier.clickable { onFiltroBaixoChange(!estado.filtroApenasBaixo) })
            }

            if (estado.carregando) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (estado.erro) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = onTentarDeNovo) { Text("Tentar de novo") }
                }
            } else if (estado.listaFiltrada.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum produto encontrado.")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(estado.listaFiltrada) { item ->
                        ItemEstoque(item = item, onClick = { onItemClick(item) })
                    }
                }
            }
        }
    }

    if (estado.produtoEmMovimentacao != null) {
        DialogoMovimentacao(
            produto = estado.produtoEmMovimentacao,
            tipo = estado.tipoMovimentacao,
            quantidade = estado.quantidadeInput,
            onTipoChange = onTipoChange,
            onQuantidadeChange = onQuantidadeChange,
            onFechar = onFecharMovimentacao,
            onConfirmar = onConfirmar
        )
    }
}

@Composable
private fun ItemEstoque(item: ProdutoItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.nome,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                if (item.estoqueBaixo) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFD32F2F))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        stringResource(R.string.estoque_baixo),
                        color = Color(0xFFD32F2F),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Qtd. Atual: ${item.quantidade}", style = MaterialTheme.typography.bodyMedium)
                Text("Mínimo: ${item.quantidadeMinima}", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { item.progressoEstoque },
                modifier = Modifier.fillMaxWidth(),
                color = if (item.estoqueBaixo) Color(0xFFD32F2F) else MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
private fun DialogoMovimentacao(
    produto: ProdutoItem,
    tipo: TipoMovimentacao,
    quantidade: String,
    onTipoChange: (TipoMovimentacao) -> Unit,
    onQuantidadeChange: (String) -> Unit,
    onFechar: () -> Unit,
    onConfirmar: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onFechar,
        title = { Text("Movimentar Estoque") },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(produto.nome, fontWeight = FontWeight.Bold)
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = tipo == TipoMovimentacao.ENTRADA,
                        onClick = { onTipoChange(TipoMovimentacao.ENTRADA) },
                        label = { Text("Entrada") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = tipo == TipoMovimentacao.SAIDA,
                        onClick = { onTipoChange(TipoMovimentacao.SAIDA) },
                        label = { Text("Saída") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = quantidade,
                    onValueChange = onQuantidadeChange,
                    label = { Text("Quantidade") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirmar, enabled = quantidade.isNotBlank()) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onFechar) {
                Text("Cancelar")
            }
        }
    )
}
