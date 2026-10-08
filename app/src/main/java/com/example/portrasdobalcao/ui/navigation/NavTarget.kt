package com.example.portrasdobalcao.ui.navigation

sealed class NavTarget(val route: String) {
    data object Splash : NavTarget("splash")
    data object Login : NavTarget("login")
    data object Principal : NavTarget("principal")
    data object ProdutoNovo : NavTarget("produto/novo")
    data object ProdutoEditar : NavTarget("produto/{id}") {
        const val ARG_ID = "id"
        fun criarRota(id: Long) = "produto/$id"
    }
    data object Relatorio : NavTarget("relatorio")
    data object Estoque : NavTarget("estoque")
    data object Perfil : NavTarget("perfil")
    data object About : NavTarget("about")

    companion object {
        /** Chave usada pela tela de produto para avisar a lista ("salvo" / "excluido"). */
        const val CHAVE_MENSAGEM = "mensagem_produto"
    }
}
