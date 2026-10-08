package com.example.portrasdobalcao.ui.features.perfil

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import com.example.portrasdobalcao.R
import com.example.portrasdobalcao.data.local.TemaApp
import com.example.portrasdobalcao.ui.components.BarraTopo
import com.example.portrasdobalcao.ui.components.CampoComRotulo
import com.example.portrasdobalcao.ui.components.CartaoApp
import com.example.portrasdobalcao.ui.components.DivisorFino
import com.example.portrasdobalcao.ui.components.SeletorSegmentado
import com.example.portrasdobalcao.ui.components.TituloSecao
import com.example.portrasdobalcao.ui.theme.PorTrasDoBalcaoTheme
import com.example.portrasdobalcao.util.formatarMoeda
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PerfilScreen(
    estado: PerfilUiState,
    onFotoEscolhida: (Uri) -> Unit,
    onTemaChange: (TemaApp) -> Unit,
    onAbrirEditarDados: () -> Unit,
    onAbrirAlterarSenha: () -> Unit,
    onAbrirSobre: () -> Unit,
    onPedirSair: () -> Unit,
    onConfirmarSair: () -> Unit,
    onFecharDialogo: () -> Unit,
    onEditNomeLojaChange: (String) -> Unit,
    onEditGestorChange: (String) -> Unit,
    onEditCategoriaChange: (String) -> Unit,
    onSalvarDados: () -> Unit,
    onSenhaAtualChange: (String) -> Unit,
    onSenhaNovaChange: (String) -> Unit,
    onSenhaConfirmarChange: (String) -> Unit,
    onSalvarSenha: () -> Unit,
    onMensagemConsumida: () -> Unit,
    onIrParaLogin: () -> Unit,
    onTentarDeNovo: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val contexto = LocalContext.current
    val irParaLogin by rememberUpdatedState(onIrParaLogin)

    LaunchedEffect(estado.irParaLogin) {
        if (estado.irParaLogin) irParaLogin()
    }

    LaunchedEffect(estado.mensagem) {
        val mensagem = estado.mensagem ?: return@LaunchedEffect
        val texto = when (mensagem) {
            MensagemPerfil.DADOS_SALVOS -> contexto.getString(R.string.perfil_dados_salvos)
            MensagemPerfil.SENHA_ALTERADA -> contexto.getString(R.string.perfil_senha_alterada)
            MensagemPerfil.ERRO_SALVAR -> contexto.getString(R.string.perfil_erro_salvar)
            MensagemPerfil.ERRO_FOTO -> contexto.getString(R.string.perfil_erro_foto)
        }
        snackbarHostState.showSnackbar(texto)
        onMensagemConsumida()
    }

    val seletorFoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) onFotoEscolhida(uri)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { BarraTopo(titulo = stringResource(R.string.perfil_titulo)) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        val usuario = estado.usuario
        when {
            estado.carregando -> Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            estado.erro || usuario == null -> Column(
                Modifier.padding(padding).fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(stringResource(R.string.comum_erro_carregar), textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onTentarDeNovo) { Text(stringResource(R.string.comum_tentar_de_novo)) }
            }

            else -> Column(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp),
            ) {
                // Cabeçalho
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Avatar(
                        fotoPath = usuario.fotoPath,
                        iniciais = usuario.iniciais,
                        onClick = {
                            seletorFoto.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(usuario.nomeLoja, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                    Text(
                        "${usuario.nomeGestor} · ${usuario.categoria}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Spacer(Modifier.height(24.dp))
                TituloSecao(stringResource(R.string.perfil_secao_resumo))
                CartaoApp(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth().padding(16.dp)) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.perfil_resumo_produtos),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                estado.totalProdutos.toString(),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.perfil_resumo_investido),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                formatarMoeda(estado.totalInvestido),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                TituloSecao(stringResource(R.string.perfil_secao_conta))
                CartaoApp(Modifier.fillMaxWidth()) {
                    LinhaMenu(Icons.Outlined.Edit, stringResource(R.string.perfil_editar_dados), onAbrirEditarDados)
                    DivisorFino()
                    LinhaMenu(Icons.Outlined.VpnKey, stringResource(R.string.perfil_alterar_senha), onAbrirAlterarSenha)
                }

                Spacer(Modifier.height(20.dp))
                TituloSecao(stringResource(R.string.perfil_secao_aparencia))
                SeletorSegmentado(
                    opcoes = listOf(
                        TemaApp.CLARO to stringResource(R.string.perfil_tema_claro),
                        TemaApp.ESCURO to stringResource(R.string.perfil_tema_escuro),
                        TemaApp.SISTEMA to stringResource(R.string.perfil_tema_sistema),
                    ),
                    selecionado = estado.tema,
                    onSelecionar = onTemaChange,
                )

                Spacer(Modifier.height(20.dp))
                TituloSecao(stringResource(R.string.perfil_secao_app))
                CartaoApp(Modifier.fillMaxWidth()) {
                    LinhaMenu(Icons.Outlined.Info, stringResource(R.string.perfil_sobre), onAbrirSobre)
                }

                Spacer(Modifier.height(20.dp))
                OutlinedButton(
                    onClick = onPedirSair,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.perfil_sair), fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    when (estado.dialogo) {
        DialogoPerfil.NENHUM -> Unit

        DialogoPerfil.SAIR -> AlertDialog(
            onDismissRequest = onFecharDialogo,
            title = { Text(stringResource(R.string.perfil_sair_titulo)) },
            text = { Text(stringResource(R.string.perfil_sair_texto)) },
            confirmButton = {
                TextButton(onClick = onConfirmarSair) {
                    Text(stringResource(R.string.perfil_sair), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = onFecharDialogo) { Text(stringResource(R.string.comum_cancelar)) } },
        )

        DialogoPerfil.EDITAR_DADOS -> AlertDialog(
            onDismissRequest = onFecharDialogo,
            title = { Text(stringResource(R.string.perfil_editar_titulo)) },
            text = {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    CampoComRotulo(
                        rotulo = stringResource(R.string.login_nome_loja),
                        valor = estado.editNomeLoja,
                        onValorChange = onEditNomeLojaChange,
                        isError = estado.erroNomeLoja != null,
                        mensagemErro = mensagemDe(estado.erroNomeLoja),
                    )
                    Spacer(Modifier.height(12.dp))
                    CampoComRotulo(
                        rotulo = stringResource(R.string.login_nome_gestor),
                        valor = estado.editGestor,
                        onValorChange = onEditGestorChange,
                        isError = estado.erroGestor != null,
                        mensagemErro = mensagemDe(estado.erroGestor),
                    )
                    Spacer(Modifier.height(12.dp))
                    CampoComRotulo(
                        rotulo = stringResource(R.string.login_categoria),
                        valor = estado.editCategoria,
                        onValorChange = onEditCategoriaChange,
                        isError = estado.erroCategoria != null,
                        mensagemErro = mensagemDe(estado.erroCategoria),
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = onSalvarDados, enabled = !estado.salvando) {
                    Text(stringResource(R.string.comum_salvar))
                }
            },
            dismissButton = { TextButton(onClick = onFecharDialogo) { Text(stringResource(R.string.comum_cancelar)) } },
        )

        DialogoPerfil.ALTERAR_SENHA -> AlertDialog(
            onDismissRequest = onFecharDialogo,
            title = { Text(stringResource(R.string.perfil_alterar_senha)) },
            text = {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    CampoComRotulo(
                        rotulo = stringResource(R.string.perfil_senha_atual),
                        valor = estado.senhaAtual,
                        onValorChange = onSenhaAtualChange,
                        isError = estado.erroSenhaAtual != null,
                        mensagemErro = mensagemDe(estado.erroSenhaAtual),
                        teclado = KeyboardOptions(keyboardType = KeyboardType.Password),
                        transformacao = PasswordVisualTransformation(),
                    )
                    Spacer(Modifier.height(12.dp))
                    CampoComRotulo(
                        rotulo = stringResource(R.string.perfil_senha_nova),
                        valor = estado.senhaNova,
                        onValorChange = onSenhaNovaChange,
                        isError = estado.erroSenhaNova != null,
                        mensagemErro = mensagemDe(estado.erroSenhaNova),
                        teclado = KeyboardOptions(keyboardType = KeyboardType.Password),
                        transformacao = PasswordVisualTransformation(),
                    )
                    Spacer(Modifier.height(12.dp))
                    CampoComRotulo(
                        rotulo = stringResource(R.string.perfil_senha_confirmar),
                        valor = estado.senhaConfirmar,
                        onValorChange = onSenhaConfirmarChange,
                        isError = estado.erroSenhaConfirmar != null,
                        mensagemErro = mensagemDe(estado.erroSenhaConfirmar),
                        teclado = KeyboardOptions(keyboardType = KeyboardType.Password),
                        transformacao = PasswordVisualTransformation(),
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = onSalvarSenha, enabled = !estado.salvando) {
                    Text(stringResource(R.string.comum_salvar))
                }
            },
            dismissButton = { TextButton(onClick = onFecharDialogo) { Text(stringResource(R.string.comum_cancelar)) } },
        )
    }
}

@Composable
private fun mensagemDe(erro: ErroPerfil?): String? = when (erro) {
    null -> null
    ErroPerfil.VAZIO -> stringResource(R.string.comum_campo_vazio)
    ErroPerfil.LOJA_DUPLICADA -> stringResource(R.string.comum_erro_loja_duplicada)
    ErroPerfil.SENHA_CURTA -> stringResource(R.string.comum_erro_senha_curta)
    ErroPerfil.SENHA_ATUAL_INCORRETA -> stringResource(R.string.perfil_erro_senha_atual)
    ErroPerfil.CONFIRMACAO_DIFERENTE -> stringResource(R.string.perfil_erro_confirmacao)
}

@Composable
private fun LinhaMenu(icone: ImageVector, texto: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icone, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(16.dp))
        Text(texto, Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Foto de perfil; sem foto, mostra as iniciais sobre a cor primária. */
@Composable
private fun Avatar(fotoPath: String?, iniciais: String, onClick: () -> Unit) {
    val imagem by produceState<ImageBitmap?>(initialValue = null, fotoPath) {
        value = if (fotoPath.isNullOrBlank()) null else withContext(Dispatchers.IO) { carregarBitmap(fotoPath) }
    }

    Box(Modifier.size(96.dp)) {
        Box(
            Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            val bitmap = imagem
            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Text(
                    iniciais,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .size(30.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.tertiary)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Default.CameraAlt,
                contentDescription = stringResource(R.string.perfil_trocar_foto),
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onTertiary,
            )
        }
    }
}

/** Lê a imagem já reduzida (no máximo ~1024px) para não estourar a memória. */
private fun carregarBitmap(caminho: String, maxLado: Int = 512): ImageBitmap? {
    val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(caminho, limites)
    var amostra = 1
    while (limites.outWidth / amostra > maxLado * 2 || limites.outHeight / amostra > maxLado * 2) amostra *= 2
    val opcoes = BitmapFactory.Options().apply { inSampleSize = amostra }
    return BitmapFactory.decodeFile(caminho, opcoes)?.asImageBitmap()
}

private val estadoPreview = PerfilUiState(
    carregando = false,
    usuario = UsuarioUi("HangHigh", "Nico Bellic", "Roupas", null),
    totalProdutos = 1,
    totalInvestido = 200.0,
)

@Composable
private fun PerfilPreviewConteudo() {
    PerfilScreen(
        estado = estadoPreview,
        onFotoEscolhida = {}, onTemaChange = {}, onAbrirEditarDados = {}, onAbrirAlterarSenha = {},
        onAbrirSobre = {}, onPedirSair = {}, onConfirmarSair = {}, onFecharDialogo = {},
        onEditNomeLojaChange = {}, onEditGestorChange = {}, onEditCategoriaChange = {}, onSalvarDados = {},
        onSenhaAtualChange = {}, onSenhaNovaChange = {}, onSenhaConfirmarChange = {}, onSalvarSenha = {},
        onMensagemConsumida = {}, onIrParaLogin = {}, onTentarDeNovo = {},
    )
}

@Preview(name = "Perfil claro", showSystemUi = true)
@Composable
private fun PerfilClaroPreview() {
    PorTrasDoBalcaoTheme(darkTheme = false) { PerfilPreviewConteudo() }
}

@Preview(name = "Perfil escuro", showSystemUi = true)
@Composable
private fun PerfilEscuroPreview() {
    PorTrasDoBalcaoTheme(darkTheme = true) { PerfilPreviewConteudo() }
}
