package com.example.portrasdobalcao.ui.features.estoque

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.seuprojeto.ui.viewmodel.EstoqueViewModel

@Composable
fun EstoqueScreen(
    viewModel: EstoqueViewModel,
    onVoltar: () -> Unit
) {
    val estoques by viewModel.estoques.collectAsState()

    Column {
        Button(onClick = onVoltar) {
            Text("Voltar")
        }

        LazyColumn {
            items(estoques) { item ->
                Row {
                    Text("Produto ID: ${item.produtoId} - Qtd: ${item.quantidade}")
                    
                    Button(onClick = { viewModel.entrada(item.produtoId, 1) }) {
                        Text("+1 Entrada")
                    }

                    Button(onClick = { viewModel.saida(item.produtoId, 1) }) {
                        Text("-1 Saída")
                    }
                }
            }
        }
    }
}