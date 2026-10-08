package com.example.portrasdobalcao.ui.features.principal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.portrasdobalcao.R
import com.example.portrasdobalcao.model.ProdutoItem
import com.example.portrasdobalcao.ui.components.BarraTopo
import com.example.portrasdobalcao.ui.components.CartaoApp
import com.example.portrasdobalcao.ui.components.SeloEstoqueBaixo
import com.example.portrasdobalcao.ui.theme.PorTrasDoBalcaoTheme

@Composable
fun PrincipalScreen(
    estado: PrincipalUiState,
    onBuscaChange: (String) -> Unit,
    onNovoProduto: () -> Unit,
    onEditarProduto: (Long) -> Unit,
    onAbrirRelatorio: () -> Unit,
    onSolicitarExclusao: (ProdutoItem) -> Unit,
    onConfirmarExclusao: () -> Unit,
    onCancelarExclusao: () -> Unit,
    onDesfazerExclusao: (ProdutoItem) -> Unit,
    onSnackbarConsumida: () -> Unit,
    onTentarDeNovo: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val contexto = LocalContext.current

    LaunchedEffect(estado.snackbar) {
        val mensagem = estado.snackbar ?: return@LaunchedEffect
        when (mensagem) {
            is SnackbarProduto.Excluido -> {
                val resultado = snackbarHostState.showSnackbar(
                    message = contexto.getString(R.string.principal_excluido, mensagem.item.nome),
                    actionLabel = contexto.getString(R.string.principal_desfazer),
                    duration = SnackbarDuration.Long,
                )
                if (resultado == SnackbarResult.ActionPerformed) onDesfazerExclusao(mensagem.item)
            }
            SnackbarProduto.Salvo ->
                snackbarHostState.showSnackbar(contexto.getString(R.string.principal_salvo))
            SnackbarProduto.ExcluidoSemDesfazer ->
                snackbarHostState.showSnackbar(contexto.getString(R.string.principal_excluido_simples))
            SnackbarProduto.ErroExcluir ->
                snackbarHostState.showSnackbar(contexto.getString(R.string.comum_erro_excluir))
            SnackbarProduto.ErroDesfazer ->
                snackbarHostState.showSnackbar(contexto.getString(R.string.principal_erro_desfazer))
        }
        onSnackbarConsumida()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BarraTopo(
                titulo = stringResource(R.string.principal_titulo),
                acoes = {
                    IconButton(onClick = onAbrirRelatorio) {
                        Icon(
                            Icons.Outlined.BarChart,
                            contentDescription = stringResource(R.string.principal_relatorio),
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNovoProduto,
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary,
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.principal_novo))
            }
        },
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            CampoBusca(valor = estado.busca, onValorChange = onBuscaChange)
            Spacer(Modifier.height(16.dp))

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

                    estado.totalCadastrados == 0 -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        EstadoVazio(onCadastrar = onNovoProduto)
                        Spacer(Modifier.height(16.dp))
                        DicaDeslizar()
                    }

                    estado.itens.isEmpty() -> Text(
                        text = stringResource(R.string.principal_sem_resultado, estado.busca.trim()),
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )

                    else -> LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(estado.itens, key = { it.id }) { item ->
                            ItemProduto(
                                item = item,
                                onClick = { onEditarProduto(item.id) },
                                onSolicitarExclusao = { onSolicitarExclusao(item) },
                            )
                        }
                        item {
                            Box(Modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                                DicaDeslizar()
                            }
                        }
                    }
                }
            }
        }
    }

    estado.pendenteExclusao?.let { item ->
        AlertDialog(
            onDismissRequest = onCancelarExclusao,
            title = { Text(stringResource(R.string.principal_excluir_titulo)) },
            text = { Text(stringResource(R.string.principal_excluir_texto, item.nome)) },
            confirmButton = {
                TextButton(onClick = onConfirmarExclusao) {
                    Text(stringResource(R.string.comum_excluir), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = onCancelarExclusao) { Text(stringResource(R.string.comum_cancelar)) }
            },
        )
    }
}

@Composable
private fun CampoBusca(valor: String, onValorChange: (String) -> Unit) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        placeholder = { Text(stringResource(R.string.principal_buscar)) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
        ),
    )
}

@Composable
private fun EstadoVazio(onCadastrar: () -> Unit) {
    CartaoApp(Modifier.fillMaxWidth()) {
        Column(
            Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))
            Icon(
                Icons.Outlined.Inventory2,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.principal_vazio),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = onCadastrar, shape = RoundedCornerShape(16.dp)) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.principal_cadastrar), fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DicaDeslizar() {
    Text(
        text = stringResource(R.string.principal_dica),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemProduto(
    item: ProdutoItem,
    onClick: () -> Unit,
    onSolicitarExclusao: () -> Unit,
) {
    // Devolve false: o item volta ao lugar e só some depois da confirmação no diálogo.
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { valor ->
            if (valor == SwipeToDismissBoxValue.EndToStart) onSolicitarExclusao()
            false
        },
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(end = 24.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.comum_excluir),
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        },
    ) {
        CartaoApp(Modifier.fillMaxWidth(), onClick = onClick) {
            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text(item.nome, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        item.categoria,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (item.estoqueBaixo) {
                        Spacer(Modifier.height(6.dp))
                        SeloEstoqueBaixo()
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        item.quantidade.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        stringResource(R.string.comum_unidade_abrev),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

private val itensPreview = listOf(
    ProdutoItem(1, "Caderno 96 folhas", "Papelaria", 1, 5, 12.5),
    ProdutoItem(2, "Caneta azul", "Papelaria", 40, 10, 1.2),
)

@Composable
private fun PrincipalPreviewConteudo(estado: PrincipalUiState) {
    PrincipalScreen(
        estado = estado,
        onBuscaChange = {}, onNovoProduto = {}, onEditarProduto = {}, onAbrirRelatorio = {},
        onSolicitarExclusao = {}, onConfirmarExclusao = {}, onCancelarExclusao = {},
        onDesfazerExclusao = {}, onSnackbarConsumida = {}, onTentarDeNovo = {},
    )
}

@Preview(name = "Principal claro", showSystemUi = true)
@Composable
private fun PrincipalClaroPreview() {
    PorTrasDoBalcaoTheme(darkTheme = false) {
        PrincipalPreviewConteudo(PrincipalUiState(carregando = false, itens = itensPreview, totalCadastrados = 2))
    }
}

@Preview(name = "Principal escuro (vazio)", showSystemUi = true)
@Composable
private fun PrincipalEscuroPreview() {
    PorTrasDoBalcaoTheme(darkTheme = true) {
        PrincipalPreviewConteudo(PrincipalUiState(carregando = false))
    }
}
