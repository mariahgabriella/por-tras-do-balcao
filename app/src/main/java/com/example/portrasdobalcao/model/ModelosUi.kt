package com.example.portrasdobalcao.model

/** Produto já juntado com seu Estoque, pronto para as telas. */
data class ProdutoItem(
    val id: Long,
    val nome: String,
    val categoria: String,
    val quantidade: Int,
    val quantidadeMinima: Int,
    val precoCusto: Double,
) {
    val estoqueBaixo: Boolean get() = quantidade <= quantidadeMinima
    val subtotal: Double get() = precoCusto * quantidade

    /** Progresso da barra: a quantidade mínima fica na metade da barra. */
    val progressoEstoque: Float
        get() = if (quantidadeMinima <= 0) {
            if (quantidade > 0) 1f else 0f
        } else {
            (quantidade.toFloat() / (quantidadeMinima * 2f)).coerceIn(0f, 1f)
        }
}

enum class TipoMovimentacao { ENTRADA, SAIDA }

sealed interface ResultadoOperacao {
    object Sucesso : ResultadoOperacao
    object QuantidadeInvalida : ResultadoOperacao
    data class EstoqueInsuficiente(val atual: Int) : ResultadoOperacao
    object Erro : ResultadoOperacao
}
