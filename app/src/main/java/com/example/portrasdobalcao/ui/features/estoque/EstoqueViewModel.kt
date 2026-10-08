package com.example.portrasdobalcao.ui.features.estoque

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portrasdobalcao.data.repository.ProdutoRepository
import com.example.portrasdobalcao.model.ProdutoItem
import com.example.portrasdobalcao.model.ResultadoOperacao
import com.example.portrasdobalcao.model.TipoMovimentacao
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstoqueUiState(
    val carregando: Boolean = true,
    val erro: Boolean = false,
    val produtos: List<ProdutoItem> = emptyList(),
    val filtroApenasBaixo: Boolean = false,
    /** ID do produto que está sendo movimentado no diálogo. */
    val produtoEmMovimentacao: ProdutoItem? = null,
    val tipoMovimentacao: TipoMovimentacao = TipoMovimentacao.ENTRADA,
    val quantidadeInput: String = "",
    val resultado: ResultadoOperacao? = null,
) {
    val listaFiltrada: List<ProdutoItem>
        get() = if (filtroApenasBaixo) produtos.filter { it.estoqueBaixo } else produtos
}

class EstoqueViewModel(private val repo: ProdutoRepository) : ViewModel() {
    private val _estado = MutableStateFlow(EstoqueUiState())
    val estado: StateFlow<EstoqueUiState> = _estado.asStateFlow()

    private var job: Job? = null

    init {
        carregar()
    }

    fun carregar() {
        job?.cancel()
        _estado.update { it.copy(carregando = true, erro = false) }
        job = viewModelScope.launch {
            repo.observarItens()
                .catch { _estado.update { s -> s.copy(carregando = false, erro = true) } }
                .collect { lista ->
                    _estado.update { it.copy(carregando = false, produtos = lista) }
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
