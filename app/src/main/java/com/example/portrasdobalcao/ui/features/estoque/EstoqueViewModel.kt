package com.example.portrasdobalcao.ui.features.estoque

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portrasdobalcao.data.repository.ProdutoRepository
import com.example.portrasdobalcao.model.ProdutoItem
import com.example.portrasdobalcao.model.ResultadoOperacao
import com.example.portrasdobalcao.model.TipoMovimentacao
import com.example.portrasdobalcao.ui.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

// O que faz: guarda o estado da tela de estoque, usando os 4 estados do UiState pra carregamento.
// Por que existe: reaproveita o mesmo UiState da tela Principal, em vez de manter dois padrões diferentes no app.
// O que quebra se sumir: a tela volta a usar carregando/erro soltos, sem a checagem do compilador.
data class EstoqueUiState(
    val tela: UiState = UiState.Carregando,
    val filtroApenasBaixo: Boolean = false,
    /** ID do produto que está sendo movimentado no diálogo. */
    val produtoEmMovimentacao: ProdutoItem? = null,
    val tipoMovimentacao: TipoMovimentacao = TipoMovimentacao.ENTRADA,
    val quantidadeInput: String = "",
    val resultado: ResultadoOperacao? = null,
) {
    val listaFiltrada: List<ProdutoItem>
        get() {
            val itens = (tela as? UiState.Sucesso)?.itens ?: return emptyList()
            return if (filtroApenasBaixo) itens.filter { it.estoqueBaixo } else itens
        }
}

class EstoqueViewModel(private val repo: ProdutoRepository) : ViewModel() {
    private val _estado = MutableStateFlow(EstoqueUiState())
    val estado: StateFlow<EstoqueUiState> = _estado.asStateFlow()

    private var job: Job? = null

    init {
        carregar()
    }

    // O que faz: decide qual UiState mostrar na tela de estoque.
    // Por que existe: segue o mesmo padrão de try/catch da tela Principal.
    // O que quebra se sumir: a tela fica sem saber se está carregando, vazia, com erro ou com sucesso.
    fun carregar() {
        job?.cancel()
        _estado.update { it.copy(tela = UiState.Carregando) }
        job = viewModelScope.launch {
            try {
                repo.observarItens().collect { lista ->
                    _estado.update {
                        it.copy(tela = if (lista.isEmpty()) UiState.Vazio else UiState.Sucesso(lista))
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

    fun onFiltroBaixoChange(apenasBaixo: Boolean) {
        _estado.update { it.copy(filtroApenasBaixo = apenasBaixo) }
    }

    fun abrirMovimentacao(produto: ProdutoItem) {
        _estado.update { it.copy(produtoEmMovimentacao = produto, quantidadeInput = "", resultado = null) }
    }

    fun fecharMovimentacao() {
        _estado.update { it.copy(produtoEmMovimentacao = null) }
    }

    fun onTipoChange(tipo: TipoMovimentacao) {
        _estado.update { it.copy(tipoMovimentacao = tipo) }
    }

    fun onQuantidadeChange(valor: String) {
        if (valor.all { it.isDigit() }) {
            _estado.update { it.copy(quantidadeInput = valor) }
        }
    }

    fun confirmar() {
        val s = _estado.value
        val produtoId = s.produtoEmMovimentacao?.id ?: return
        val qtd = s.quantidadeInput.toIntOrNull() ?: 0

        viewModelScope.launch {
            val res = repo.movimentar(produtoId, s.tipoMovimentacao, qtd)
            _estado.update { it.copy(resultado = res) }
            if (res is ResultadoOperacao.Sucesso) {
                fecharMovimentacao()
            }
        }
    }

    fun resultadoConsumido() {
        _estado.update { it.copy(resultado = null) }
    }
}
