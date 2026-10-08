package com.example.portrasdobalcao.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.text.NumberFormat
import java.text.Normalizer
import java.util.Locale
import kotlin.coroutines.cancellation.CancellationException

/** Formata um valor em reais, ex.: 35.0 -> "R$ 35,00". */
fun formatarMoeda(valor: Double): String =
    NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valor)

/** Minúsculas e sem acentos, usado na busca por nome. */
fun String.semAcentoMinusculo(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .lowercase()

/** Executa uma operação de banco sem deixar a exceção derrubar o app. */
suspend fun <T> operacaoSegura(bloco: suspend () -> T): Result<T> =
    try {
        Result.success(bloco())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

/** Cria uma ViewModelProvider.Factory a partir de uma lambda (sem Hilt). */
fun <VM : ViewModel> fabrica(criar: () -> VM): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = criar() as T
    }
