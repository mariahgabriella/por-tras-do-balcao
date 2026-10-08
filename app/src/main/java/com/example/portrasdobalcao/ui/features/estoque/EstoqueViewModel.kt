package com.example.portrasdobalcao.ui.features.estoque

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portrasdobalcao.model.ProdutoItem
import com.example.portrasdobalcao.model.ResultadoOperacao
import com.example.portrasdobalcao.model.TipoMovimentacao
import com.example.portrasdobalcao.data.repository.ProdutoRepository
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
    val somenteBaixo: Boolean = false,
    val itens: List<ProdutoItem> = emptyList(),
    val totalCadastrados: Int = 0,
    val itemSelecionado: ProdutoItem? = null,
    val tipo: TipoMovimentacao = TipoMovimentacao.ENTRADA,
    val quantidadeTexto: String = "",
    val processando: Boolean = false,
    /** Resultado da última operação, mostrado em Snackbar pela tela. */
    val resultado: ResultadoOperacao? = null,
) {
    val quantidadeDigitada: Int? get() = quantidadeTexto.toIntOrNull()

    /** Saldo após a operação; null quando a quantidade é inválida ou o saldo ficaria negativo. */
    val saldoDepois: Int?
        get() {
            val item = itemSelecionado ?: return null
            val q = quantidadeDigitada?.takeIf { it > 0 } ?: return null
            val saldo = when (tipo) {
                TipoMovimentacao.ENTRADA -> item.quantidade.toLong() + q
                TipoMovimentacao.SAIDA -> item.quantidade.toLong() - q
            }
            return if (saldo < 0 || saldo > Int.MAX_VALUE) null else saldo.toInt()
        }
}

class EstoqueViewModel(private val repo: ProdutoRepository) : ViewModel() {
    private val _estado = MutableStateFlow(EstoqueUiState())
    val estado: StateFlow<EstoqueUiState> = _estado.asStateFlow()

    private var todos: List<ProdutoItem> = emptyList()
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
                    todos = lista
                    _estado.update { s ->
                        s.copy(
                            carregando = false,
                            erro = false,
                            itens = filtrar(lista, s.somenteBaixo),
                            totalCadastrados = lista.size,
                            // mantém o sheet aberto com o saldo atualizado
                            itemSelecionado = s.itemSelecionado?.let { sel -> lista.firstOrNull { it.id == sel.id } },
                        )
                    }
                }
        }
    }

    fun onFiltroBaixoChange(ativo: Boolean) {
        _estado.update { it.copy(somenteBaixo = ativo, itens = filtrar(todos, ativo)) }
    }

    fun abrirMovimentacao(item: ProdutoItem) {
        _estado.update {
            it.copy(itemSelecionado = item, tipo = TipoMovimentacao.ENTRADA, quantidadeTexto = "")
        }
    }

    fun fecharMovimentacao() = _estado.update { it.copy(itemSelecionado = null, quantidadeTexto = "") }
    fun onTipoChange(tipo: TipoMovimentacao) = _estado.update { it.copy(tipo = tipo) }
    fun onQuantidadeChange(texto: String) =
        _estado.update { it.copy(quantidadeTexto = texto.filter { c -> c in '0'..'9' }.take(9)) }

    fun confirmar() {
        val s = _estado.value
        val item = s.itemSelecionado ?: return
        if (s.processando) return

        val quantidade = s.quantidadeDigitada ?: 0
        _estado.update { it.copy(processando = true) }
        viewModelScope.launch {
            val resultado = repo.movimentar(item.id, s.tipo, quantidade)
            _estado.update {
                it.copy(
                    processando = false,
                    resultado = resultado,
                    // só fecha o sheet quando deu certo
                    itemSelecionado = if (resultado is ResultadoOperacao.Sucesso) null else it.itemSelecionado,
                    quantidadeTexto = if (resultado is ResultadoOperacao.Sucesso) "" else it.quantidadeTexto,
                )
            }
        }
    }

    fun resultadoConsumido() = _estado.update { it.copy(resultado = null) }

    private fun filtrar(lista: List<ProdutoItem>, somenteBaixo: Boolean): List<ProdutoItem> =
        if (somenteBaixo) lista.filter { it.estoqueBaixo } else lista
}
