package com.example.portrasdobalcao.ui.features.produto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portrasdobalcao.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

enum class CampoForm { NOME, CATEGORIA, QUANTIDADE, MINIMA, CUSTO }
enum class ErroCampo { VAZIO, CUSTO_INVALIDO, QUANTIDADE_INVALIDA }
enum class MensagemForm { ERRO_SALVAR, ERRO_EXCLUIR }
enum class ConclusaoForm { SALVO, EXCLUIDO }

data class ProdutoFormUiState(
    val editando: Boolean = false,
    val carregando: Boolean = false,
    val erroCarregar: Boolean = false,
    val naoEncontrado: Boolean = false,
    val nome: String = "",
    val categoria: String = "",
    val quantidade: String = "",
    val minima: String = "",
    val custo: String = "",
    val erroNome: ErroCampo? = null,
    val erroCategoria: ErroCampo? = null,
    val erroQuantidade: ErroCampo? = null,
    val erroMinima: ErroCampo? = null,
    val erroCusto: ErroCampo? = null,
    val salvando: Boolean = false,
    val confirmandoExclusao: Boolean = false,
    val mensagem: MensagemForm? = null,
    val concluido: ConclusaoForm? = null,
)

/** produtoId == null: modo novo. Caso contrário: modo edição. */
class ProdutoFormViewModel(
    private val repo: ProdutoRepository,
    private val produtoId: Long?,
) : ViewModel() {
    private val _estado = MutableStateFlow(ProdutoFormUiState(editando = produtoId != null))
    val estado: StateFlow<ProdutoFormUiState> = _estado.asStateFlow()

    init {
        if (produtoId != null) carregarProduto(produtoId)
    }

    fun carregarProduto(id: Long) {
        _estado.update { it.copy(carregando = true, erroCarregar = false, naoEncontrado = false) }
        viewModelScope.launch {
            repo.buscarItem(id).fold(
                onSuccess = { item ->
                    if (item == null) {
                        _estado.update { it.copy(carregando = false, naoEncontrado = true) }
                    } else {
                        _estado.update {
                            it.copy(
                                carregando = false,
                                nome = item.nome,
                                categoria = item.categoria,
                                quantidade = item.quantidade.toString(),
                                minima = item.quantidadeMinima.toString(),
                                custo = String.format(Locale("pt", "BR"), "%.2f", item.precoCusto),
                            )
                        }
                    }
                },
                onFailure = { _estado.update { it.copy(carregando = false, erroCarregar = true) } },
            )
        }
    }

    fun tentarCarregarDeNovo() {
        if (produtoId != null) carregarProduto(produtoId)
    }

    fun onCampoChange(campo: CampoForm, texto: String) {
        _estado.update {
            when (campo) {
                CampoForm.NOME -> it.copy(nome = texto, erroNome = null)
                CampoForm.CATEGORIA -> it.copy(categoria = texto, erroCategoria = null)
                CampoForm.QUANTIDADE -> it.copy(quantidade = apenasDigitos(texto), erroQuantidade = null)
                CampoForm.MINIMA -> it.copy(minima = apenasDigitos(texto), erroMinima = null)
                CampoForm.CUSTO -> it.copy(custo = filtrarDecimal(texto), erroCusto = null)
            }
        }
    }

    /** Validação ao sair do campo. */
    fun onCampoPerdeuFoco(campo: CampoForm) {
        _estado.update { s ->
            val erro = validar(campo, s)
            when (campo) {
                CampoForm.NOME -> s.copy(erroNome = erro)
                CampoForm.CATEGORIA -> s.copy(erroCategoria = erro)
                CampoForm.QUANTIDADE -> s.copy(erroQuantidade = erro)
                CampoForm.MINIMA -> s.copy(erroMinima = erro)
                CampoForm.CUSTO -> s.copy(erroCusto = erro)
            }
        }
    }

    fun salvar() {
        val s = _estado.value
        if (s.salvando) return // impede duplo toque

        val novo = s.copy(
            erroNome = validar(CampoForm.NOME, s),
            erroCategoria = validar(CampoForm.CATEGORIA, s),
            erroQuantidade = validar(CampoForm.QUANTIDADE, s),
            erroMinima = validar(CampoForm.MINIMA, s),
            erroCusto = validar(CampoForm.CUSTO, s),
        )
        val temErro = listOf(
            novo.erroNome, novo.erroCategoria, novo.erroQuantidade, novo.erroMinima, novo.erroCusto,
        ).any { it != null }
        if (temErro) {
            _estado.value = novo
            return
        }

        _estado.value = novo.copy(salvando = true)
        viewModelScope.launch {
            repo.salvar(
                id = produtoId,
                nome = s.nome.trim(),
                categoria = s.categoria.trim(),
                quantidade = s.quantidade.toInt(),
                minima = s.minima.toInt(),
                custo = s.custo.replace(',', '.').toDouble(),
            ).fold(
                onSuccess = { _estado.update { it.copy(salvando = false, concluido = ConclusaoForm.SALVO) } },
                // mantém o que foi digitado
                onFailure = { _estado.update { it.copy(salvando = false, mensagem = MensagemForm.ERRO_SALVAR) } },
            )
        }
    }

    fun pedirExclusao() = _estado.update { it.copy(confirmandoExclusao = true) }
    fun cancelarExclusao() = _estado.update { it.copy(confirmandoExclusao = false) }

    fun confirmarExclusao() {
        val id = produtoId ?: return
        if (_estado.value.salvando) return
        _estado.update { it.copy(confirmandoExclusao = false, salvando = true) }
        viewModelScope.launch {
            repo.excluir(id).fold(
                onSuccess = { _estado.update { it.copy(salvando = false, concluido = ConclusaoForm.EXCLUIDO) } },
                onFailure = { _estado.update { it.copy(salvando = false, mensagem = MensagemForm.ERRO_EXCLUIR) } },
            )
        }
    }

    fun mensagemConsumida() = _estado.update { it.copy(mensagem = null) }

    private fun validar(campo: CampoForm, s: ProdutoFormUiState): ErroCampo? = when (campo) {
        CampoForm.NOME -> if (s.nome.isBlank()) ErroCampo.VAZIO else null
        CampoForm.CATEGORIA -> if (s.categoria.isBlank()) ErroCampo.VAZIO else null
        CampoForm.QUANTIDADE -> validarInteiro(s.quantidade)
        CampoForm.MINIMA -> validarInteiro(s.minima)
        CampoForm.CUSTO -> {
            if (s.custo.isBlank()) {
                ErroCampo.VAZIO
            } else {
                val valor = s.custo.replace(',', '.').toDoubleOrNull()
                if (valor == null || valor <= 0.0) ErroCampo.CUSTO_INVALIDO else null
            }
        }
    }

    private fun validarInteiro(texto: String): ErroCampo? {
        if (texto.isBlank()) return ErroCampo.VAZIO
        val valor = texto.toIntOrNull()
        return if (valor == null || valor < 0) ErroCampo.QUANTIDADE_INVALIDA else null
    }

    private fun apenasDigitos(texto: String): String = texto.filter { it in '0'..'9' }

    /** Aceita dígitos e um único separador (vírgula ou ponto). */
    private fun filtrarDecimal(texto: String): String {
        var viuSeparador = false
        return buildString {
            for (c in texto) {
                if (c in '0'..'9') {
                    append(c)
                } else if ((c == ',' || c == '.') && !viuSeparador) {
                    viuSeparador = true
                    append(c)
                }
            }
        }
    }
}
