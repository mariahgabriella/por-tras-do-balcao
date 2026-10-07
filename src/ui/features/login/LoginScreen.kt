package com.example.portrasdobalcao.ui.features.login

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.portrasdobalcao.data.local.SessaoManager
import com.example.portrasdobalcao.model.AppDatabase

@Composable
fun LoginScreen(
    onLoginSucesso: () -> Unit,
    context: Context = LocalContext.current,
    viewModel: LoginViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val db = AppDatabase.getInstance(context)
                val sessao = SessaoManager(context)
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(db.usuarioDao(), sessao) as T
            }
        }
    )
) {
    if (viewModel.carregando) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val u = viewModel.usuario

        if (u == null) {
            Text("Criar conta", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.nomeLoja,
                onValueChange = { viewModel.nomeLoja = it; viewModel.erro = "" },
                label = { Text("Nome da loja") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.nomeGestor,
                onValueChange = { viewModel.nomeGestor = it; viewModel.erro = "" },
                label = { Text("Nome do gestor") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.categoriaLoja,
                onValueChange = { viewModel.categoriaLoja = it; viewModel.erro = "" },
                label = { Text("Categoria da loja") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.senha,
                onValueChange = { viewModel.senha = it; viewModel.erro = "" },
                label = { Text("Senha (min. 4 caracteres)") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (viewModel.erro.isNotEmpty()) {
                Text(
                    text = viewModel.erro,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.cadastrarEEntrar(onLoginSucesso) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Criar e entrar")
            }
        } else {
            Text("Entrar", style = MaterialTheme.typography.headlineMedium)
            Text(u.nomeLoja, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.senha,
                onValueChange = { viewModel.senha = it; viewModel.erro = "" },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (viewModel.erro.isNotEmpty()) {
                Text(
                    text = viewModel.erro,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.entrar(onLoginSucesso) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar")
            }
        }
    }
}
