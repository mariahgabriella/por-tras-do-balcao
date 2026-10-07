package com.example.portrasdobalcao.ui.features.fornecedor

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.portrasdobalcao.model.Fornecedor

@Composable
fun FornecedorScreen(
    fornecedores: List<Fornecedor>,
    onAdicionar: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAdicionar) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(fornecedores) { fornecedor ->
                ListItem(
                    headlineContent = { Text(fornecedor.nome) },
                    supportingContent = { Text(fornecedor.categoriaFornecida) }
                )
            }
        }
    }
}
