package com.example.portrasdobalcao.data.local

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class TemaApp { CLARO, ESCURO, SISTEMA }

/** Guarda o tema escolhido em SharedPreferences, como o SessaoManager faz. */
class TemaPreferences(context: Context) {
    private val prefs = context.applicationContext
        .getSharedPreferences("preferencias_app", Context.MODE_PRIVATE)

    private val _tema = MutableStateFlow(ler())
    val tema: StateFlow<TemaApp> = _tema.asStateFlow()

    fun salvar(novo: TemaApp) {
        prefs.edit().putString(CHAVE_TEMA, novo.name).apply()
        _tema.value = novo
    }

    private fun ler(): TemaApp =
        runCatching { TemaApp.valueOf(prefs.getString(CHAVE_TEMA, null) ?: TemaApp.SISTEMA.name) }
            .getOrDefault(TemaApp.SISTEMA)

    private companion object {
        const val CHAVE_TEMA = "tema"
    }
}
