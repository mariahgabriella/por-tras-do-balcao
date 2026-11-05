package com.example.portrasdobalcao.ui.features.principal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portrasdobalcao.model.ProdutoItem
import com.example.portrasdobalcao.data.repository.ProdutoRepository
import com.example.portrasdobalcao.ui.UiState
import com.example.portrasdobalcao.util.semAcentoMinusculo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface SnackbarProduto {
    object Salvo : SnackbarProduto
    object ExcluidoSemDesfazer : SnackbarProduto
    data class Excluido(val item: ProdutoItem) : SnackbarProduto
    object ErroExcluir : SnackbarProduto
    object ErroDesfazer : SnackbarProduto
}

// O que faz: guarda o estado da tela principal, usando os 4 estados do UiState.
// Por que existe: substitui os campos soltos carregando/erro/itens por um único campo "tela".
// O que quebra se sumir: a tela volta a decidir o estado com booleans soltos, sem checagem do compilador.
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

    // O que faz: decide qual dos 4 UiState mostrar, buscando os dados do repositório.
    // Por que existe: concentra o try/catch num só lugar, longe da tela.
    // O que quebra se sumir: a tela fica sem saber se está carregando, vazia, com erro ou com sucesso.
    fun carregar() {
        job?.cancel()
        _estado.update { it.copy(tela = UiState.Carregando) }
        job = viewModelScope.launch {
            try {
                //delay(2000)
                //repo.observarItens().collect { lista ->
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

    // O que faz: atualiza o texto da busca e refiltra a lista, só quando já existe uma lista de sucesso.
    // Por que existe: evita tentar filtrar quando a tela ainda está carregando ou com erro.
    // O que quebra se sumir: a busca para de funcionar, ou tenta filtrar uma lista que não existe.
    fun onBuscaChange(texto: String) {
        _estado.update { estado ->
            val novaTela = if (estado.tela is UiState.Sucesso) {
                UiState.Sucesso(filtrar(todos, texto))
            } else {
                estado.tela
            }
            estado.copy(busca = texto, tela = novaTela)
        }
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
