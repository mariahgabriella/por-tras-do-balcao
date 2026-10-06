package com.example.portrasdobalcao.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


class SessaoManager(context: Context) {
  
    private val preferences: SharedPreferences =
        context.getSharedPreferences("sessao", Context.MODE_PRIVATE)

    companion object {
        private const val CHAVE_ESTA_LOGADO = "esta_logado"
        private const val CHAVE_USUARIO_ID = "usuario_id"
    }

    fun estaLogado(): Boolean {
        return preferences.getBoolean(CHAVE_ESTA_LOGADO, false)
    }

    fun iniciarSessao(usuarioId: Long = 0L) {
        preferences.edit {
            putBoolean(CHAVE_ESTA_LOGADO, true)
            putLong(CHAVE_USUARIO_ID, usuarioId)
        }
    }


    fun encerrarSessao() {
        preferences.edit {
            remove(CHAVE_ESTA_LOGADO)
            remove(CHAVE_USUARIO_ID)
        }
    }

    fun getUsuarioId(): Long? {
        if (!estaLogado()) return null
        val id = preferences.getLong(CHAVE_USUARIO_ID, -1L)
        return if (id != -1L) id else null
    }
}
