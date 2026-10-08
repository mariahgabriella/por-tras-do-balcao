package com.example.portrasdobalcao.data.repository

import com.example.portrasdobalcao.model.Estoque
import com.example.portrasdobalcao.model.EstoqueDao
import kotlinx.coroutines.flow.Flow

class EstoqueRepository(private val dao: EstoqueDao) {

    val todos: Flow<List<Estoque>> = dao.listarTodos()

    suspend fun inserir(estoque: Estoque) = dao.inserir(estoque)

    suspend fun atualizar(estoque: Estoque) = dao.atualizar(estoque)

    suspend fun buscarPorProduto(produtoId: Long): Estoque? = dao.buscarPorProduto(produtoId)
}
