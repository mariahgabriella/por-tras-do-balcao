package com.example.portrasdobalcao.ui.features.relatorio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portrasdobalcao.model.ProdutoItem
import com.example.portrasdobalcao.data.repository.ProdutoRepository
import com.example.portrasdobalcao.ui.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

// O que faz: guarda o estado da tela de relatório, usando os 4 estados do UiState.
// Por que existe: evita guardar totais calculados separados da lista, que podem ficar dessincronizados.
// O que quebra se sumir: a tela volta a mostrar carregando/erro soltos, e os totais voltam a ser guardados à parte.
data class RelatorioUiState(
    val tela: UiState = UiState.Carregando,
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
        _estado.update { it.copy(tela = UiState.Carregando) }
        job = viewModelScope.launch {
            try {
                repo.observarItens().collect { lista ->
                    val ordenada = lista.sortedByDescending { it.subtotal }
                    _estado.update {
                        it.copy(tela = if (lista.isEmpty()) UiState.Vazio else UiState.Sucesso(ordenada))
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                _estado.update { it.copy(tela = UiState.Erro("Sem conexão. Verifique a internet e tente novamente.")) }
            } catch (e: Exception) {
                _estado.update { it.copy(tela = UiState.Erro("Não foi possível carregar os dados.")) }
            }
        }
    }
}
