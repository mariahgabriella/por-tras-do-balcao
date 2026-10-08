package com.example.portrasdobalcao.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.portrasdobalcao.R

private val FormaCartao = RoundedCornerShape(16.dp)

@Composable
fun DivisorFino(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outlineVariant)
    )
}

/** Barra superior no estilo dos prints: título em negrito e linha fina embaixo. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraTopo(
    titulo: String,
    modifier: Modifier = Modifier,
    onVoltar: (() -> Unit)? = null,
    acoes: @Composable RowScope.() -> Unit = {},
) {
    Column(modifier) {
        TopAppBar(
            title = { Text(titulo, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                if (onVoltar != null) {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.comum_voltar),
                        )
                    }
                }
            },
            actions = acoes,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                actionIconContentColor = MaterialTheme.colorScheme.onBackground,
            ),
        )
        DivisorFino()
    }
}

/** Card padrão do app (cantos 16dp, borda fina). Se onClick != null, o card é clicável. */
@Composable
fun CartaoApp(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    content: @Composable ColumnScope.() -> Unit,
) {
    val cores = CardDefaults.cardColors(containerColor = containerColor)
    val borda = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    if (onClick == null) {
        Card(modifier = modifier, shape = FormaCartao, colors = cores, border = borda, content = content)
    } else {
        Card(onClick = onClick, modifier = modifier, shape = FormaCartao, colors = cores, border = borda, content = content)
    }
}

@Composable
fun LogoApp(
    modifier: Modifier = Modifier,
    tamanho: Dp = 80.dp,
    fundo: Color = MaterialTheme.colorScheme.primary,
    conteudo: Color = MaterialTheme.colorScheme.onPrimary,
) {
    Box(
        modifier
            .size(tamanho)
            .clip(RoundedCornerShape(tamanho / 4))
            .background(fundo),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_logo_balcao),
            contentDescription = stringResource(R.string.comum_logo),
            tint = conteudo,
            modifier = Modifier.size(tamanho * 0.55f),
        )
    }
}

/** Campo com o rótulo ACIMA, como no print do Login. */
@Composable
fun CampoComRotulo(
    rotulo: String,
    valor: String,
    onValorChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    mensagemErro: String? = null,
    teclado: KeyboardOptions = KeyboardOptions.Default,
    transformacao: VisualTransformation = VisualTransformation.None,
    iconeFinal: (@Composable () -> Unit)? = null,
    prefixo: String? = null,
    enabled: Boolean = true,
    onPerdeuFoco: (() -> Unit)? = null,
) {
    var jaFocou by remember { mutableStateOf(false) }
    Column(modifier) {
        Text(rotulo, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { foco ->
                    if (foco.isFocused) {
                        jaFocou = true
                    } else if (jaFocou) {
                        jaFocou = false
                        onPerdeuFoco?.invoke()
                    }
                },
            enabled = enabled,
            singleLine = true,
            shape = FormaCartao,
            isError = isError,
            placeholder = placeholder?.let { { Text(it) } },
            prefix = prefixo?.let { { Text(it) } },
            trailingIcon = iconeFinal,
            supportingText = mensagemErro?.let { { Text(it) } },
            keyboardOptions = teclado,
            visualTransformation = transformacao,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            ),
        )
    }
}

/** Selo "Estoque baixo" usado na Principal e no Estoque. */
@Composable
fun SeloEstoqueBaixo(modifier: Modifier = Modifier) {
    Row(
        modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Default.Warning,
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
        )
        Spacer(Modifier.width(4.dp))
        Text(
            stringResource(R.string.comum_estoque_baixo),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    }
}

/** Seletor segmentado (Claro/Escuro/Do Sistema e Entrada/Saída). */
@Composable
fun <T> SeletorSegmentado(
    opcoes: List<Pair<T, String>>,
    selecionado: T,
    onSelecionar: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val forma = RoundedCornerShape(16.dp)
    Row(
        modifier
            .fillMaxWidth()
            .clip(forma)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, forma)
            .padding(4.dp),
    ) {
        opcoes.forEach { (valor, rotulo) ->
            val marcado = valor == selecionado
            Box(
                Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (marcado) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .selectable(selected = marcado, role = Role.RadioButton, onClick = { onSelecionar(valor) })
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    rotulo,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (marcado) FontWeight.Bold else FontWeight.Normal,
                    color = if (marcado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun TituloSecao(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto.uppercase(),
        modifier = modifier.padding(start = 4.dp, bottom = 8.dp),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
