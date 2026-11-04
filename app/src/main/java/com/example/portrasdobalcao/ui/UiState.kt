// O que faz: define os 4 estados possíveis da tela principal.
// Por que existe: evita tela em branco quando carregar() falha ou demora.
// O que quebra se sumir: a tela volta a decidir o estado com booleans soltos, sem checagem do compilador.

package com.example.portrasdobalcao.ui

import com.example.portrasdobalcao.model.ProdutoItem

sealed interface UiState {
    data object Carregando : UiState
    data object Vazio : UiState
    data class Erro(val mensagem: String) : UiState
    data class Sucesso(val itens: List<ProdutoItem>) : UiState
}
