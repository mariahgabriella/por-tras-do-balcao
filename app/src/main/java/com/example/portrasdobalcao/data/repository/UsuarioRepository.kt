package com.example.portrasdobalcao.data.repository

import android.content.Context
import android.net.Uri
import com.example.portrasdobalcao.model.UsuarioDao
import com.example.portrasdobalcao.model.Usuario
import com.example.portrasdobalcao.data.seguranca.SenhaHasher
import com.example.portrasdobalcao.util.operacaoSegura
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File

class UsuarioRepository(
    private val dao: UsuarioDao,
    private val context: Context,
) {
    suspend fun buscarPrimeiro(): Result<Usuario?> = operacaoSegura { dao.buscarPrimeiroUsuario() }

    suspend fun listarTodos(): Result<List<Usuario>> = operacaoSegura { dao.listarTodos() }

    suspend fun deletarPorNome(nome: String): Result<Int> = operacaoSegura { dao.deletarPorNome(nome) }

    fun observarPorId(id: Long): Flow<Usuario?> = dao.observarPorId(id)

    /** Falha com SQLiteConstraintException se o nome da loja já existir (índice único). */
    suspend fun criarConta(
        nomeLoja: String,
        nomeGestor: String,
        categoria: String,
        senha: String,
    ): Result<Long> = operacaoSegura {
        dao.inserir(
            Usuario(
                nomeLoja = nomeLoja,
                nomeGestor = nomeGestor,
                categoriaLoja = categoria,
                senha = SenhaHasher.gerarHash(senha),
            )
        )
    }

    /**
     * Confere a senha. Se o usuário antigo ainda tem senha em texto puro e ela confere,
     * já salva o hash (migração feita no primeiro login depois da atualização).
     */
    suspend fun autenticar(id: Long, senha: String): Result<Boolean> = operacaoSegura {
        val u = dao.buscarPorId(id) ?: return@operacaoSegura false
        if (SenhaHasher.estaEmHash(u.senha)) {
            SenhaHasher.verificar(senha, u.senha)
        } else if (u.senha == senha) {
            dao.atualizar(u.copy(senha = SenhaHasher.gerarHash(senha)))
            true
        } else {
            false
        }
    }

    suspend fun atualizarDados(
        id: Long,
        nomeLoja: String,
        nomeGestor: String,
        categoria: String,
    ): Result<Unit> = operacaoSegura {
        val u = dao.buscarPorId(id) ?: error("Usuário não encontrado")
        dao.atualizar(u.copy(nomeLoja = nomeLoja, nomeGestor = nomeGestor, categoriaLoja = categoria))
        Unit
    }

    /** Devolve false quando a senha atual está incorreta. */
    suspend fun alterarSenha(id: Long, atual: String, nova: String): Result<Boolean> = operacaoSegura {
        val u = dao.buscarPorId(id) ?: return@operacaoSegura false
        val confere = if (SenhaHasher.estaEmHash(u.senha)) {
            SenhaHasher.verificar(atual, u.senha)
        } else {
            u.senha == atual
        }
        if (!confere) {
            false
        } else {
            dao.atualizar(u.copy(senha = SenhaHasher.gerarHash(nova)))
            true
        }
    }

    /** Copia a imagem escolhida para o filesDir do app e salva o caminho no Usuario. */
    suspend fun salvarFoto(id: Long, origem: Uri): Result<Unit> = operacaoSegura {
        withContext(Dispatchers.IO) {
            val destino = File(context.filesDir, "perfil_${id}_${System.currentTimeMillis()}.jpg")
            val entrada = context.contentResolver.openInputStream(origem) ?: error("Não foi possível abrir a imagem")
            entrada.use { dentro -> destino.outputStream().use { fora -> dentro.copyTo(fora) } }

            val u = dao.buscarPorId(id) ?: error("Usuário não encontrado")
            val antigo = u.fotoPerfilPath
            dao.atualizar(u.copy(fotoPerfilPath = destino.absolutePath))
            if (!antigo.isNullOrBlank() && antigo != destino.absolutePath) File(antigo).delete()
        }
    }
}
