package com.example.portrasdobalcao.ui.features.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.portrasdobalcao.data.local.SessaoManager
import com.example.portrasdobalcao.model.AppDatabase
import com.example.portrasdobalcao.model.Usuario

@Composable
fun PerfilScreen(
    onSair: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var usuario by remember { mutableStateOf<Usuario?>(null) }

    LaunchedEffect(Unit) {
        val sessaoManager = SessaoManager(context)
        val id = sessaoManager.getUsuarioId()
        if (id != null) {
            val db = AppDatabase.getInstance(context)
            usuario = db.usuarioDao().buscarPorId(id)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Perfil", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        usuario?.let { u ->
            Text("Loja: ${u.nomeLoja}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Gestor: ${u.nomeGestor}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Categoria: ${u.categoriaLoja}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(24.dp))
        }

        Button(
            onClick = {
                val sessaoManager = SessaoManager(context)
                sessaoManager.encerrarSessao()
                onSair()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sair")
        }
    }
}
