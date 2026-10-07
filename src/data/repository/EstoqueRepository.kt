package com.seuprojeto.data.repository // TROQUE pelo pacote real do projeto

import com.seuprojeto.data.dao.EstoqueDao
import com.seuprojeto.data.entity.Estoque
import kotlinx.coroutines.flow.Flow

class EstoqueRepository(private val dao: EstoqueDao) {

    val todos: Flow<List<Estoque>> = dao.listarTodos()

    suspend fun inserir(estoque: Estoque) = dao.inserir(estoque)

    suspend fun atualizar(estoque: Estoque) = dao.atualizar(estoque)

    suspend fun buscarPorProduto(produtoId: Int): Estoque? = dao.buscarPorProduto(produtoId)
}
