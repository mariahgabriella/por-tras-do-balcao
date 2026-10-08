package com.example.portrasdobalcao.ui.features.login

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.portrasdobalcao.R
import com.example.portrasdobalcao.ui.components.CampoComRotulo
import com.example.portrasdobalcao.ui.components.LogoApp
import com.example.portrasdobalcao.ui.theme.PorTrasDoBalcaoTheme

@Composable
fun LoginScreen(
    estado: LoginUiState,
    onNomeLojaChange: (String) -> Unit,
    onNomeGestorChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onSenhaChange: (String) -> Unit,
    onAlternarSenhaVisivel: () -> Unit,
    onEnviar: () -> Unit,
    onTentarDeNovo: () -> Unit,
    onLoginConcluido: () -> Unit,
) {
    val concluido by rememberUpdatedState(onLoginConcluido)
    LaunchedEffect(estado.loginConcluido) {
        if (estado.loginConcluido) concluido()
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .imePadding(),
        contentAlignment = Alignment.TopCenter,
    ) {
        when (estado.modo) {
            ModoLogin.CARREGANDO -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            ModoLogin.ERRO -> Column(
                Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(stringResource(R.string.comum_erro_carregar), textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onTentarDeNovo) { Text(stringResource(R.string.comum_tentar_de_novo)) }
            }

            ModoLogin.CRIAR_CONTA, ModoLogin.ENTRAR -> Formulario(
                estado, onNomeLojaChange, onNomeGestorChange, onCategoriaChange,
                onSenhaChange, onAlternarSenhaVisivel, onEnviar,
            )
        }
    }
}

@Composable
private fun Formulario(
    estado: LoginUiState,
    onNomeLojaChange: (String) -> Unit,
    onNomeGestorChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onSenhaChange: (String) -> Unit,
    onAlternarSenhaVisivel: () -> Unit,
    onEnviar: () -> Unit,
) {
    val criando = estado.modo == ModoLogin.CRIAR_CONTA

    val mensagemLoja = if (estado.erro == ErroLogin.LOJA_DUPLICADA) {
        stringResource(R.string.comum_erro_loja_duplicada)
    } else null
    val mensagemSenha = when (estado.erro) {
        ErroLogin.SENHA_CURTA -> stringResource(R.string.comum_erro_senha_curta)
        ErroLogin.SENHA_INCORRETA -> stringResource(R.string.login_erro_senha_incorreta)
        else -> null
    }
    val mensagemGeral = when (estado.erro) {
        ErroLogin.CAMPO_VAZIO -> stringResource(R.string.comum_campo_vazio)
        ErroLogin.GENERICO -> stringResource(R.string.login_erro_generico)
        else -> null
    }

    Column(
        Modifier
            .widthIn(max = 420.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LogoApp()
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(if (criando) R.string.login_titulo_criar else R.string.login_titulo_entrar),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = if (criando) {
                stringResource(R.string.login_subtitulo_criar)
            } else {
                stringResource(R.string.login_subtitulo_entrar, estado.nomeLojaExistente)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(32.dp))

        if (criando) {
            CampoComRotulo(
                rotulo = stringResource(R.string.login_nome_loja),
                valor = estado.nomeLoja,
                onValorChange = onNomeLojaChange,
                isError = estado.nomeLojaVazio || estado.erro == ErroLogin.LOJA_DUPLICADA,
                mensagemErro = mensagemLoja,
                enabled = !estado.carregando,
            )
            Spacer(Modifier.height(16.dp))
            CampoComRotulo(
                rotulo = stringResource(R.string.login_nome_gestor),
                valor = estado.nomeGestor,
                onValorChange = onNomeGestorChange,
                isError = estado.nomeGestorVazio,
                enabled = !estado.carregando,
            )
            Spacer(Modifier.height(16.dp))
            CampoComRotulo(
                rotulo = stringResource(R.string.login_categoria),
                valor = estado.categoria,
                onValorChange = onCategoriaChange,
                placeholder = stringResource(R.string.login_categoria_dica),
                isError = estado.categoriaVazia,
                enabled = !estado.carregando,
            )
            Spacer(Modifier.height(16.dp))
        }

        CampoComRotulo(
            rotulo = stringResource(if (criando) R.string.login_senha_criar else R.string.login_senha),
            valor = estado.senha,
            onValorChange = onSenhaChange,
            isError = estado.senhaVazia ||
                estado.erro == ErroLogin.SENHA_CURTA ||
                estado.erro == ErroLogin.SENHA_INCORRETA,
            mensagemErro = mensagemSenha,
            teclado = KeyboardOptions(keyboardType = KeyboardType.Password),
            transformacao = if (estado.senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
            enabled = !estado.carregando,
            iconeFinal = {
                IconButton(onClick = onAlternarSenhaVisivel) {
                    Icon(
                        imageVector = if (estado.senhaVisivel) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = stringResource(
                            if (estado.senhaVisivel) R.string.login_ocultar_senha else R.string.login_mostrar_senha
                        ),
                    )
                }
            },
        )

        if (mensagemGeral != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = mensagemGeral,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onEnviar,
            enabled = !estado.carregando,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            if (estado.carregando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = LocalContentColor.current,
                )
            } else {
                Text(
                    stringResource(if (criando) R.string.login_botao_criar else R.string.login_botao_entrar),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun LoginPreviewConteudo(estado: LoginUiState) {
    LoginScreen(
        estado = estado,
        onNomeLojaChange = {}, onNomeGestorChange = {}, onCategoriaChange = {},
        onSenhaChange = {}, onAlternarSenhaVisivel = {}, onEnviar = {},
        onTentarDeNovo = {}, onLoginConcluido = {},
    )
}

private val estadoPreview = LoginUiState(
    modo = ModoLogin.CRIAR_CONTA,
    nomeLojaVazio = true,
    erro = ErroLogin.CAMPO_VAZIO,
)

@Preview(name = "Login claro", showSystemUi = true)
@Composable
private fun LoginClaroPreview() {
    PorTrasDoBalcaoTheme(darkTheme = false) { LoginPreviewConteudo(estadoPreview) }
}

@Preview(name = "Login escuro", showSystemUi = true)
@Composable
private fun LoginEscuroPreview() {
    PorTrasDoBalcaoTheme(darkTheme = true) { LoginPreviewConteudo(estadoPreview) }
}
