package com.example.portrasdobalcao.ui.features.relatorio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portrasdobalcao.model.ProdutoItem
import com.example.portrasdobalcao.data.repository.ProdutoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RelatorioUiState(
    val carregando: Boolean = true,
    val erro: Boolean = false,
    val totalInvestido: Double = 0.0,
    val quantidadeProdutos: Int = 0,
    val totalItens: Int = 0,
    /** Ordenados pelo valor investido, maior primeiro. */
    val produtos: List<ProdutoItem> = emptyList(),
)

class RelatorioViewModel(private val repo: ProdutoRepository) : ViewModel() {
    private val _estado = MutableStateFlow(RelatorioUiState())
    val estado: StateFlow<RelatorioUiState> = _estado.asStateFlow()

    private var job: Job? = null

    init {
        carregar()
    }

    /** Os valores se atualizam sozinhos: o repositório entrega um Flow do banco. */
    fun carregar() {
        job?.cancel()
        _estado.update { it.copy(carregando = true, erro = false) }
        job = viewModelScope.launch {
            repo.observarItens()
                .catch { _estado.update { s -> s.copy(carregando = false, erro = true) } }
                .collect { lista ->
                    _estado.value = RelatorioUiState(
                        carregando = false,
                        totalInvestido = lista.sumOf { it.subtotal },
                        quantidadeProdutos = lista.size,
                        totalItens = lista.sumOf { it.quantidade },
                        produtos = lista.sortedByDescending { it.subtotal },
                    )
                }
        }
    }
}
