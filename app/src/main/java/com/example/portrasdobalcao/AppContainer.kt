package com.example.portrasdobalcao

import android.content.Context
import com.example.portrasdobalcao.data.AppDatabase
import com.example.portrasdobalcao.data.local.SessaoManager
import com.example.portrasdobalcao.data.local.TemaPreferences
import com.example.portrasdobalcao.data.repository.EstoqueRepository
import com.example.portrasdobalcao.data.repository.ProdutoRepository
import com.example.portrasdobalcao.data.repository.UsuarioRepository

/**
 * Cria uma vez os repositórios usados pelos ViewModels (sem biblioteca nova).
 * Acessado por AppContainer.getInstance(context), no mesmo estilo do AppDatabase.getInstance.
 */
class AppContainer private constructor(context: Context) {
    private val app = context.applicationContext
    private val db = AppDatabase.getInstance(app)

    val usuarioRepository = UsuarioRepository(db.usuarioDao(), app)
    val produtoRepository = ProdutoRepository(db)
    val estoqueRepository = EstoqueRepository(db.estoqueDao())
    val temaPreferences = TemaPreferences(app)
    val sessaoManager = SessaoManager(app)

    companion object {
        @Volatile
        private var instancia: AppContainer? = null

        fun getInstance(context: Context): AppContainer =
            instancia ?: synchronized(this) {
                instancia ?: AppContainer(context).also { instancia = it }
            }
    }
}
