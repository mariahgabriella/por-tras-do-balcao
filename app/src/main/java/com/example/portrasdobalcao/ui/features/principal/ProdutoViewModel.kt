// O que faz: decide qual dos 4 UiState mostrar, buscando os dados do repositório.
// Por que existe: concentra o try/catch num só lugar, longe da tela.
// O que quebra se sumir: a tela fica sem saber se está carregando, vazia, com erro ou com sucesso.

package com.example.portrasdobalcao.ui.features.principal

import com.example.portrasdobalcao.ui.UiState
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
import java.io.IOException
import kotlinx.coroutines.CancellationException

sealed interface SnackbarProduto {
    object Salvo : SnackbarProduto
    object ExcluidoSemDesfazer : SnackbarProduto
    data class Excluido(val item: ProdutoItem) : SnackbarProduto
    object ErroExcluir : SnackbarProduto
    object ErroDesfazer : SnackbarProduto
}
data class PrincipalUiState(
    val tela: UiState = UiState.Carregando,
    val busca: String = "",
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
    _estado.update { it.copy(tela = UiState.Carregando) }
    job = viewModelScope.launch {
        try {
            repo.observarItens().collect { lista ->
                todos = lista
                val buscaAtual = _estado.value.busca
                val novaTela = if (lista.isEmpty()) {
                    UiState.Vazio
                } else {
                    UiState.Sucesso(filtrar(lista, buscaAtual))
                }
                _estado.update { it.copy(tela = novaTela) }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            _estado.update {
                it.copy(tela = UiState.Erro("Sem conexão. Verifique a internet e tente novamente."))
            }
        } catch (e: Exception) {
            _estado.update {
                it.copy(tela = UiState.Erro("Não foi possível carregar os dados."))
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
