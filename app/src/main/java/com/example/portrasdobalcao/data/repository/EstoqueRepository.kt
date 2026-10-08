package com.example.portrasdobalcao.data.repository

import com.example.portrasdobalcao.model.Estoque
import com.example.portrasdobalcao.model.EstoqueDao
import com.example.portrasdobalcao.model.ProdutoItem
import com.example.portrasdobalcao.model.ResultadoOperacao
import com.example.portrasdobalcao.model.TipoMovimentacao
import kotlinx.coroutines.flow.Flow

class EstoqueRepository(private val estoqueDao: EstoqueDao) {

    val produtosComEstoque: Flow<List<ProdutoItem>> = estoqueDao.getProdutosComEstoque()

    suspend fun movimentarEstoque(
        produtoId: Long,
        quantidade: Int,
        tipo: TipoMovimentacao
    ): ResultadoOperacao {
        if (quantidade <= 0) return ResultadoOperacao.QuantidadeInvalida

        val atual = estoqueDao.buscarPorProduto(produtoId)?.quantidade ?: 0
        
        if (tipo == TipoMovimentacao.SAIDA && atual < quantidade) {
            return ResultadoOperacao.EstoqueInsuficiente(atual)
        }

        val delta = if (tipo == TipoMovimentacao.ENTRADA) quantidade else -quantidade
        estoqueDao.movimentarEstoque(produtoId, delta)
        return ResultadoOperacao.Sucesso
    }

    suspend fun configurarAlerta(produtoId: Long, quantidadeMinima: Int) {
        val estoque = estoqueDao.buscarPorProduto(produtoId)
            ?: Estoque(produtoId = produtoId, quantidade = 0)
        
        estoqueDao.inserir(estoque.copy(
            quantidadeMinima = quantidadeMinima,
            ultimaAtualizacao = System.currentTimeMillis()
        ))
    }
}
