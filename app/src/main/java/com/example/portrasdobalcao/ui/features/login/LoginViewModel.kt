package com.example.portrasdobalcao.ui.features.login

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portrasdobalcao.data.repository.UsuarioRepository
import com.example.portrasdobalcao.data.local.SessaoManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ModoLogin { CARREGANDO, CRIAR_CONTA, ENTRAR, ERRO }

enum class ErroLogin { CAMPO_VAZIO, SENHA_CURTA, LOJA_DUPLICADA, SENHA_INCORRETA, GENERICO }

data class LoginUiState(
    val modo: ModoLogin = ModoLogin.CARREGANDO,
    val nomeLojaExistente: String = "",
    val nomeLoja: String = "",
    val nomeGestor: String = "",
    val categoria: String = "",
    val senha: String = "",
    val senhaVisivel: Boolean = false,
    val carregando: Boolean = false,
    val nomeLojaVazio: Boolean = false,
    val nomeGestorVazio: Boolean = false,
    val categoriaVazia: Boolean = false,
    val senhaVazia: Boolean = false,
    val erro: ErroLogin? = null,
    val loginConcluido: Boolean = false,
)

class LoginViewModel(
    private val repo: UsuarioRepository,
    private val sessao: SessaoManager,
) : ViewModel() {
    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado.asStateFlow()

    private var usuarioId: Long? = null

    init {
        verificarUsuario()
    }

    /** Decide o modo da tela: sem usuário no banco = "Criar conta"; com usuário = "Entrar". */
    fun verificarUsuario() {
        _estado.update { it.copy(modo = ModoLogin.CARREGANDO) }
        viewModelScope.launch {
            repo.buscarPrimeiro().fold(
                onSuccess = { usuario ->
                    usuarioId = usuario?.id
                    _estado.update {
                        it.copy(
                            modo = if (usuario == null) ModoLogin.CRIAR_CONTA else ModoLogin.ENTRAR,
                            nomeLojaExistente = usuario?.nomeLoja.orEmpty(),
                        )
                    }
                },
                onFailure = { _estado.update { it.copy(modo = ModoLogin.ERRO) } },
            )
        }
    }

    fun onNomeLojaChange(v: String) = _estado.update { it.copy(nomeLoja = v, nomeLojaVazio = false, erro = null) }
    fun onNomeGestorChange(v: String) = _estado.update { it.copy(nomeGestor = v, nomeGestorVazio = false, erro = null) }
    fun onCategoriaChange(v: String) = _estado.update { it.copy(categoria = v, categoriaVazia = false, erro = null) }
    fun onSenhaChange(v: String) = _estado.update { it.copy(senha = v, senhaVazia = false, erro = null) }
    fun onAlternarSenhaVisivel() = _estado.update { it.copy(senhaVisivel = !it.senhaVisivel) }

    fun onEnviar() {
        val s = _estado.value
        if (s.carregando) return
        when (s.modo) {
            ModoLogin.CRIAR_CONTA -> criarConta(s)
            ModoLogin.ENTRAR -> entrar(s)
            else -> Unit
        }
    }

    private fun criarConta(s: LoginUiState) {
        val loja = s.nomeLoja.trim()
        val gestor = s.nomeGestor.trim()
        val categoria = s.categoria.trim()

        if (loja.isEmpty() || gestor.isEmpty() || categoria.isEmpty() || s.senha.isEmpty()) {
            _estado.update {
                it.copy(
                    nomeLojaVazio = loja.isEmpty(),
                    nomeGestorVazio = gestor.isEmpty(),
                    categoriaVazia = categoria.isEmpty(),
                    senhaVazia = s.senha.isEmpty(),
                    erro = ErroLogin.CAMPO_VAZIO,
                )
            }
            return
        }
        if (s.senha.length < TAMANHO_MINIMO_SENHA) {
            _estado.update { it.copy(erro = ErroLogin.SENHA_CURTA) }
            return
        }

        _estado.update { it.copy(carregando = true, erro = null) }
        viewModelScope.launch {
            repo.criarConta(loja, gestor, categoria, s.senha).fold(
                onSuccess = { id -> concluir(id) },
                onFailure = { e ->
                    _estado.update {
                        it.copy(
                            carregando = false,
                            erro = if (e is SQLiteConstraintException) ErroLogin.LOJA_DUPLICADA else ErroLogin.GENERICO,
                        )
                    }
                },
            )
        }
    }

    private fun entrar(s: LoginUiState) {
        if (s.senha.isEmpty()) {
            _estado.update { it.copy(senhaVazia = true, erro = ErroLogin.CAMPO_VAZIO) }
            return
        }
        val id = usuarioId
        if (id == null) {
            _estado.update { it.copy(erro = ErroLogin.GENERICO) }
            return
        }

        _estado.update { it.copy(carregando = true, erro = null) }
        viewModelScope.launch {
            repo.autenticar(id, s.senha).fold(
                onSuccess = { confere ->
                    if (confere) {
                        concluir(id)
                    } else {
                        _estado.update { it.copy(carregando = false, erro = ErroLogin.SENHA_INCORRETA) }
                    }
                },
                onFailure = { _estado.update { it.copy(carregando = false, erro = ErroLogin.GENERICO) } },
            )
        }
    }

    private fun concluir(id: Long) {
        sessao.iniciarSessao(id)
        _estado.update { it.copy(carregando = false, loginConcluido = true) }
    }

    private companion object {
        const val TAMANHO_MINIMO_SENHA = 4
    }
}
