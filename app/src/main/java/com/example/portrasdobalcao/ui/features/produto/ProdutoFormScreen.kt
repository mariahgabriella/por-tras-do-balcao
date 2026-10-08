package com.example.portrasdobalcao.ui.features.produto

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.portrasdobalcao.R
import com.example.portrasdobalcao.ui.components.BarraTopo
import com.example.portrasdobalcao.ui.components.CampoComRotulo
import com.example.portrasdobalcao.ui.theme.PorTrasDoBalcaoTheme

@Composable
fun ProdutoFormScreen(
    estado: ProdutoFormUiState,
    onCampoChange: (CampoForm, String) -> Unit,
    onCampoPerdeuFoco: (CampoForm) -> Unit,
    onSalvar: () -> Unit,
    onCancelar: () -> Unit,
    onPedirExclusao: () -> Unit,
    onConfirmarExclusao: () -> Unit,
    onCancelarExclusao: () -> Unit,
    onMensagemConsumida: () -> Unit,
    onConcluido: (ConclusaoForm) -> Unit,
    onTentarCarregarDeNovo: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val contexto = LocalContext.current
    val concluir by rememberUpdatedState(onConcluido)

    LaunchedEffect(estado.concluido) {
        estado.concluido?.let { concluir(it) }
    }

    LaunchedEffect(estado.mensagem) {
        val mensagem = estado.mensagem ?: return@LaunchedEffect
        val texto = when (mensagem) {
            MensagemForm.ERRO_SALVAR -> contexto.getString(R.string.produto_erro_salvar)
            MensagemForm.ERRO_EXCLUIR -> contexto.getString(R.string.comum_erro_excluir)
        }
        snackbarHostState.showSnackbar(texto)
        onMensagemConsumida()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BarraTopo(
                titulo = stringResource(
                    if (estado.editando) R.string.produto_titulo_editar else R.string.produto_titulo_novo
                ),
                onVoltar = onCancelar,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        when {
            estado.carregando -> Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            estado.erroCarregar -> Column(
                Modifier.padding(padding).fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(stringResource(R.string.comum_erro_carregar), textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onTentarCarregarDeNovo) { Text(stringResource(R.string.comum_tentar_de_novo)) }
            }

            else -> Column(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 24.dp),
            ) {
                CampoComRotulo(
                    rotulo = stringResource(R.string.produto_nome),
                    valor = estado.nome,
                    onValorChange = { onCampoChange(CampoForm.NOME, it) },
                    isError = estado.erroNome != null,
                    mensagemErro = mensagemDe(estado.erroNome),
                    onPerdeuFoco = { onCampoPerdeuFoco(CampoForm.NOME) },
                )
                Spacer(Modifier.height(16.dp))
                CampoComRotulo(
                    rotulo = stringResource(R.string.produto_categoria),
                    valor = estado.categoria,
                    onValorChange = { onCampoChange(CampoForm.CATEGORIA, it) },
                    isError = estado.erroCategoria != null,
                    mensagemErro = mensagemDe(estado.erroCategoria),
                    onPerdeuFoco = { onCampoPerdeuFoco(CampoForm.CATEGORIA) },
                )
                Spacer(Modifier.height(16.dp))
                CampoComRotulo(
                    rotulo = stringResource(R.string.produto_quantidade),
                    valor = estado.quantidade,
                    onValorChange = { onCampoChange(CampoForm.QUANTIDADE, it) },
                    isError = estado.erroQuantidade != null,
                    mensagemErro = mensagemDe(estado.erroQuantidade),
                    teclado = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onPerdeuFoco = { onCampoPerdeuFoco(CampoForm.QUANTIDADE) },
                )
                Spacer(Modifier.height(16.dp))
                CampoComRotulo(
                    rotulo = stringResource(R.string.produto_minima),
                    valor = estado.minima,
                    onValorChange = { onCampoChange(CampoForm.MINIMA, it) },
                    isError = estado.erroMinima != null,
                    mensagemErro = mensagemDe(estado.erroMinima),
                    teclado = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onPerdeuFoco = { onCampoPerdeuFoco(CampoForm.MINIMA) },
                )
                Spacer(Modifier.height(16.dp))
                CampoComRotulo(
                    rotulo = stringResource(R.string.produto_custo),
                    valor = estado.custo,
                    onValorChange = { onCampoChange(CampoForm.CUSTO, it) },
                    prefixo = stringResource(R.string.produto_prefixo_moeda),
                    isError = estado.erroCusto != null,
                    mensagemErro = mensagemDe(estado.erroCusto),
                    teclado = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    onPerdeuFoco = { onCampoPerdeuFoco(CampoForm.CUSTO) },
                )

                Spacer(Modifier.height(28.dp))
                Button(
                    onClick = onSalvar,
                    enabled = !estado.salvando,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    if (estado.salvando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp,
                            color = LocalContentColor.current,
                        )
                    } else {
                        Text(stringResource(R.string.comum_salvar), fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onCancelar,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text(stringResource(R.string.comum_cancelar))
                }
                if (estado.editando) {
                    Spacer(Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = onPedirExclusao,
                        enabled = !estado.salvando,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    ) {
                        Text(stringResource(R.string.comum_excluir), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (estado.confirmandoExclusao) {
        AlertDialog(
            onDismissRequest = onCancelarExclusao,
            title = { Text(stringResource(R.string.produto_excluir_titulo)) },
            text = { Text(stringResource(R.string.produto_excluir_texto)) },
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

    if (estado.naoEncontrado) {
        AlertDialog(
            onDismissRequest = onCancelar,
            text = { Text(stringResource(R.string.produto_nao_encontrado)) },
            confirmButton = {
                TextButton(onClick = onCancelar) { Text(stringResource(R.string.comum_ok)) }
            },
        )
    }
}

@Composable
private fun mensagemDe(erro: ErroCampo?): String? = when (erro) {
    null -> null
    ErroCampo.VAZIO -> stringResource(R.string.comum_campo_vazio)
    ErroCampo.CUSTO_INVALIDO -> stringResource(R.string.produto_erro_custo)
    ErroCampo.QUANTIDADE_INVALIDA -> stringResource(R.string.produto_erro_quantidade)
}

@Composable
private fun FormPreviewConteudo() {
    ProdutoFormScreen(
        estado = ProdutoFormUiState(
            editando = true,
            nome = "Caderno 96 folhas",
            categoria = "Papelaria",
            quantidade = "10",
            minima = "",
            custo = "0",
            erroMinima = ErroCampo.VAZIO,
            erroCusto = ErroCampo.CUSTO_INVALIDO,
        ),
        onCampoChange = { _, _ -> }, onCampoPerdeuFoco = {}, onSalvar = {}, onCancelar = {},
        onPedirExclusao = {}, onConfirmarExclusao = {}, onCancelarExclusao = {},
        onMensagemConsumida = {}, onConcluido = {}, onTentarCarregarDeNovo = {},
    )
}

@Preview(name = "Produto claro", showSystemUi = true)
@Composable
private fun FormClaroPreview() {
    PorTrasDoBalcaoTheme(darkTheme = false) { FormPreviewConteudo() }
}

@Preview(name = "Produto escuro", showSystemUi = true)
@Composable
private fun FormEscuroPreview() {
    PorTrasDoBalcaoTheme(darkTheme = true) { FormPreviewConteudo() }
}
