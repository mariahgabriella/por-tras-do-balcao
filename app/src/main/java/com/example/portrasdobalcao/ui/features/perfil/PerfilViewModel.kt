package com.example.portrasdobalcao.ui.features.perfil

import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portrasdobalcao.data.local.TemaApp
import com.example.portrasdobalcao.data.local.TemaPreferences
import com.example.portrasdobalcao.data.repository.ProdutoRepository
import com.example.portrasdobalcao.data.repository.UsuarioRepository
import com.example.portrasdobalcao.data.local.SessaoManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UsuarioUi(
    val nomeLoja: String,
    val nomeGestor: String,
    val categoria: String,
    val fotoPath: String?,
) {
    /** Primeira letra das duas primeiras palavras do nome da loja. */
    val iniciais: String
        get() = nomeLoja.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }.take(2)
            .joinToString("") { it.first().uppercase() }
}

enum class DialogoPerfil { NENHUM, EDITAR_DADOS, ALTERAR_SENHA, SAIR }
enum class ErroPerfil { VAZIO, LOJA_DUPLICADA, SENHA_CURTA, SENHA_ATUAL_INCORRETA, CONFIRMACAO_DIFERENTE }
enum class MensagemPerfil { DADOS_SALVOS, SENHA_ALTERADA, ERRO_SALVAR, ERRO_FOTO }

data class PerfilUiState(
    val carregando: Boolean = true,
    val erro: Boolean = false,
    val usuario: UsuarioUi? = null,
    val totalProdutos: Int = 0,
    val totalInvestido: Double = 0.0,
    val tema: TemaApp = TemaApp.SISTEMA,
    val dialogo: DialogoPerfil = DialogoPerfil.NENHUM,
    val salvando: Boolean = false,
    // editar dados
    val editNomeLoja: String = "",
    val editGestor: String = "",
    val editCategoria: String = "",
    val erroNomeLoja: ErroPerfil? = null,
    val erroGestor: ErroPerfil? = null,
    val erroCategoria: ErroPerfil? = null,
    // alterar senha
    val senhaAtual: String = "",
    val senhaNova: String = "",
    val senhaConfirmar: String = "",
    val erroSenhaAtual: ErroPerfil? = null,
    val erroSenhaNova: ErroPerfil? = null,
    val erroSenhaConfirmar: ErroPerfil? = null,
    val mensagem: MensagemPerfil? = null,
    /** true = a tela deve voltar ao Login (sair, ou sem sessão). */
    val irParaLogin: Boolean = false,
)

class PerfilViewModel(
    private val usuarioRepo: UsuarioRepository,
    private val produtoRepo: ProdutoRepository,
    private val temaPrefs: TemaPreferences,
    private val sessao: SessaoManager,
) : ViewModel() {
    private val usuarioId: Long? = sessao.getUsuarioId()

    private val _estado = MutableStateFlow(
        PerfilUiState(
            carregando = usuarioId != null,
            tema = temaPrefs.tema.value,
            irParaLogin = usuarioId == null, // id nulo: vai para o Login
        )
    )
    val estado: StateFlow<PerfilUiState> = _estado.asStateFlow()

    private var job: Job? = null

    init {
        if (usuarioId != null) observar(usuarioId)
    }

    private fun observar(id: Long) {
        job?.cancel()
        _estado.update { it.copy(carregando = true, erro = false) }
        job = viewModelScope.launch {
            combine(usuarioRepo.observarPorId(id), produtoRepo.observarItens()) { usuario, itens ->
                usuario to itens
            }
                .catch { _estado.update { s -> s.copy(carregando = false, erro = true) } }
                .collect { (usuario, itens) ->
                    if (usuario == null) {
                        _estado.update { it.copy(carregando = false, irParaLogin = true) }
                    } else {
                        _estado.update {
                            it.copy(
                                carregando = false,
                                erro = false,
                                usuario = UsuarioUi(
                                    nomeLoja = usuario.nomeLoja,
                                    nomeGestor = usuario.nomeGestor,
                                    categoria = usuario.categoriaLoja,
                                    fotoPath = usuario.fotoPerfilPath,
                                ),
                                totalProdutos = itens.size,
                                totalInvestido = itens.sumOf { i -> i.subtotal },
                            )
                        }
                    }
                }
        }
    }

    fun tentarDeNovo() {
        usuarioId?.let { observar(it) }
    }

    // ---------- Foto ----------
    fun onFotoEscolhida(uri: Uri) {
        val id = usuarioId ?: return
        viewModelScope.launch {
            usuarioRepo.salvarFoto(id, uri).onFailure {
                _estado.update { it.copy(mensagem = MensagemPerfil.ERRO_FOTO) }
            }
        }
    }

    // ---------- Tema ----------
    fun onTemaChange(tema: TemaApp) {
        temaPrefs.salvar(tema)
        _estado.update { it.copy(tema = tema) }
    }

    // ---------- Diálogos ----------
    fun fecharDialogo() = _estado.update { it.copy(dialogo = DialogoPerfil.NENHUM, salvando = false) }

    fun abrirEditarDados() {
        val u = _estado.value.usuario ?: return
        _estado.update {
            it.copy(
                dialogo = DialogoPerfil.EDITAR_DADOS,
                editNomeLoja = u.nomeLoja,
                editGestor = u.nomeGestor,
                editCategoria = u.categoria,
                erroNomeLoja = null, erroGestor = null, erroCategoria = null,
            )
        }
    }

    fun onEditNomeLojaChange(v: String) = _estado.update { it.copy(editNomeLoja = v, erroNomeLoja = null) }
    fun onEditGestorChange(v: String) = _estado.update { it.copy(editGestor = v, erroGestor = null) }
    fun onEditCategoriaChange(v: String) = _estado.update { it.copy(editCategoria = v, erroCategoria = null) }

    fun salvarDados() {
        val id = usuarioId ?: return
        val s = _estado.value
        if (s.salvando) return
        val loja = s.editNomeLoja.trim()
        val gestor = s.editGestor.trim()
        val categoria = s.editCategoria.trim()

        if (loja.isEmpty() || gestor.isEmpty() || categoria.isEmpty()) {
            _estado.update {
                it.copy(
                    erroNomeLoja = if (loja.isEmpty()) ErroPerfil.VAZIO else null,
                    erroGestor = if (gestor.isEmpty()) ErroPerfil.VAZIO else null,
                    erroCategoria = if (categoria.isEmpty()) ErroPerfil.VAZIO else null,
                )
            }
            return
        }

        _estado.update { it.copy(salvando = true) }
        viewModelScope.launch {
            usuarioRepo.atualizarDados(id, loja, gestor, categoria).fold(
                onSuccess = {
                    _estado.update {
                        it.copy(salvando = false, dialogo = DialogoPerfil.NENHUM, mensagem = MensagemPerfil.DADOS_SALVOS)
                    }
                },
                onFailure = { e ->
                    if (e is SQLiteConstraintException) {
                        _estado.update { it.copy(salvando = false, erroNomeLoja = ErroPerfil.LOJA_DUPLICADA) }
                    } else {
                        _estado.update { it.copy(salvando = false, mensagem = MensagemPerfil.ERRO_SALVAR) }
                    }
                },
            )
        }
    }

    fun abrirAlterarSenha() {
        _estado.update {
            it.copy(
                dialogo = DialogoPerfil.ALTERAR_SENHA,
                senhaAtual = "", senhaNova = "", senhaConfirmar = "",
                erroSenhaAtual = null, erroSenhaNova = null, erroSenhaConfirmar = null,
            )
        }
    }

    fun onSenhaAtualChange(v: String) = _estado.update { it.copy(senhaAtual = v, erroSenhaAtual = null) }
    fun onSenhaNovaChange(v: String) = _estado.update { it.copy(senhaNova = v, erroSenhaNova = null) }
    fun onSenhaConfirmarChange(v: String) = _estado.update { it.copy(senhaConfirmar = v, erroSenhaConfirmar = null) }

    fun salvarSenha() {
        val id = usuarioId ?: return
        val s = _estado.value
        if (s.salvando) return

        val erroAtual = if (s.senhaAtual.isEmpty()) ErroPerfil.VAZIO else null
        val erroNova = when {
            s.senhaNova.isEmpty() -> ErroPerfil.VAZIO
            s.senhaNova.length < TAMANHO_MINIMO_SENHA -> ErroPerfil.SENHA_CURTA
            else -> null
        }
        val erroConfirmar = when {
            s.senhaConfirmar.isEmpty() -> ErroPerfil.VAZIO
            s.senhaConfirmar != s.senhaNova -> ErroPerfil.CONFIRMACAO_DIFERENTE
            else -> null
        }
        if (erroAtual != null || erroNova != null || erroConfirmar != null) {
            _estado.update {
                it.copy(erroSenhaAtual = erroAtual, erroSenhaNova = erroNova, erroSenhaConfirmar = erroConfirmar)
            }
            return
        }

        _estado.update { it.copy(salvando = true) }
        viewModelScope.launch {
            usuarioRepo.alterarSenha(id, s.senhaAtual, s.senhaNova).fold(
                onSuccess = { confere ->
                    if (confere) {
                        _estado.update {
                            it.copy(
                                salvando = false,
                                dialogo = DialogoPerfil.NENHUM,
                                mensagem = MensagemPerfil.SENHA_ALTERADA,
                            )
                        }
                    } else {
                        _estado.update { it.copy(salvando = false, erroSenhaAtual = ErroPerfil.SENHA_ATUAL_INCORRETA) }
                    }
                },
                onFailure = { _estado.update { it.copy(salvando = false, mensagem = MensagemPerfil.ERRO_SALVAR) } },
            )
        }
    }

    // ---------- Sair ----------
    fun pedirSair() = _estado.update { it.copy(dialogo = DialogoPerfil.SAIR) }

    fun confirmarSair() {
        sessao.encerrarSessao()
        _estado.update { it.copy(dialogo = DialogoPerfil.NENHUM, irParaLogin = true) }
    }

    fun mensagemConsumida() = _estado.update { it.copy(mensagem = null) }

    private companion object {
        const val TAMANHO_MINIMO_SENHA = 4
    }
}
