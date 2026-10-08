package com.example.portrasdobalcao.ui.features.principal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portrasdobalcao.model.ProdutoItem
import com.example.portrasdobalcao.data.repository.ProdutoRepository
import com.example.portrasdobalcao.util.semAcentoMinusculo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface SnackbarProduto {
    object Salvo : SnackbarProduto
    object ExcluidoSemDesfazer : SnackbarProduto
    data class Excluido(val item: ProdutoItem) : SnackbarProduto
    object ErroExcluir : SnackbarProduto
    object ErroDesfazer : SnackbarProduto
}

data class PrincipalUiState(
    val carregando: Boolean = true,
    val erro: Boolean = false,
    val busca: String = "",
    val itens: List<ProdutoItem> = emptyList(),
    val totalCadastrados: Int = 0,
    val pendenteExclusao: ProdutoItem? = null,
    val snackbar: SnackbarProduto? = null,
)

class ProdutoViewModel(private val repo: ProdutoRepository) : ViewModel() {
    private val _estado = MutableStateFlow(PrincipalUiState())
    val estado: StateFlow<PrincipalUiState> = _estado.asStateFlow()

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
                            itens = filtrar(lista, s.busca),
                            totalCadastrados = lista.size,
                        )
                    }
                }
        }
    }

    fun onBuscaChange(texto: String) {
        _estado.update { it.copy(busca = texto, itens = filtrar(todos, texto)) }
    }

    fun solicitarExclusao(item: ProdutoItem) = _estado.update { it.copy(pendenteExclusao = item) }
    fun cancelarExclusao() = _estado.update { it.copy(pendenteExclusao = null) }

    fun confirmarExclusao() {
        val item = _estado.value.pendenteExclusao ?: return
        _estado.update { it.copy(pendenteExclusao = null) }
        viewModelScope.launch {
            repo.excluir(item.id).fold(
                onSuccess = { _estado.update { it.copy(snackbar = SnackbarProduto.Excluido(item)) } },
                onFailure = { _estado.update { it.copy(snackbar = SnackbarProduto.ErroExcluir) } },
            )
        }
    }

    fun desfazerExclusao(item: ProdutoItem) {
        viewModelScope.launch {
            repo.restaurar(item).onFailure {
                _estado.update { it.copy(snackbar = SnackbarProduto.ErroDesfazer) }
            }
        }
    }

    /** Mensagens vindas da tela de cadastro/edição ("salvo" ou "excluido"). */
    fun mostrarMensagemDoFormulario(tipo: String) {
        val msg = when (tipo) {
            MSG_SALVO -> SnackbarProduto.Salvo
            MSG_EXCLUIDO -> SnackbarProduto.ExcluidoSemDesfazer
            else -> return
        }
        _estado.update { it.copy(snackbar = msg) }
    }

    fun snackbarConsumida() = _estado.update { it.copy(snackbar = null) }

    private fun filtrar(lista: List<ProdutoItem>, busca: String): List<ProdutoItem> {
        val termo = busca.trim().semAcentoMinusculo()
        return if (termo.isEmpty()) lista else lista.filter { it.nome.semAcentoMinusculo().contains(termo) }
    }

    companion object {
        const val MSG_SALVO = "salvo"
        const val MSG_EXCLUIDO = "excluido"
    }
}
