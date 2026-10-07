package com.example.portrasdobalcao.ui.features.estoque

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.seuprojeto.data.entity.Estoque
import com.seuprojeto.data.repository.EstoqueRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EstoqueViewModel(private val repo: EstoqueRepository) : ViewModel() {

    val estoques: StateFlow<List<Estoque>> = repo.todos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    /** Entrada: soma a quantidade; cria o registro se o produto ainda não tem estoque. */
    fun entrada(produtoId: Int, quantidade: Int) {
        if (quantidade <= 0) return
        viewModelScope.launch {
            val atual = repo.buscarPorProduto(produtoId)
            if (atual == null) {
                repo.inserir(Estoque(produtoId = produtoId, quantidade = quantidade))
            } else {
                repo.atualizar(atual.copy(quantidade = atual.quantidade + quantidade))
            }
        }
    }

    /** Saída: subtrai a quantidade, sem permitir estoque negativo. */
    fun saida(produtoId: Int, quantidade: Int) {
        if (quantidade <= 0) return
        viewModelScope.launch {
            val atual = repo.buscarPorProduto(produtoId) ?: return@launch
            if (atual.quantidade >= quantidade) {
                repo.atualizar(atual.copy(quantidade = atual.quantidade - quantidade))
            }
        }
    }
}

/** Factory simples. Se o projeto usa Hilt/Koin, use a injeção que já existe. */
class EstoqueViewModelFactory(private val repo: EstoqueRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = EstoqueViewModel(repo) as T
}
