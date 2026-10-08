package com.example.portrasdobalcao.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EstoqueDao {
    @Query("""
        SELECT p.id, p.nome, p.categoria, e.quantidade, e.quantidadeMinima, p.precoCusto
        FROM produtos p
        LEFT JOIN estoque e ON p.id = e.produtoId
        WHERE p.ativo = 1
    """)
    fun getProdutosComEstoque(): Flow<List<ProdutoItem>>

    @Query("SELECT * FROM estoque WHERE produtoId = :produtoId")
    suspend fun buscarPorProduto(produtoId: Long): Estoque?

    @Query("SELECT * FROM estoque")
    fun listarTodos(): Flow<List<Estoque>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(estoque: Estoque)

    @Update
    suspend fun atualizar(estoque: Estoque)

    @Transaction
    suspend fun movimentarEstoque(produtoId: Long, delta: Int) {
        val estoque = buscarPorProduto(produtoId) ?: Estoque(produtoId = produtoId, quantidade = 0)
        val novaQuantidade = (estoque.quantidade + delta).coerceAtLeast(0)
        inserir(estoque.copy(quantidade = novaQuantidade, ultimaAtualizacao = System.currentTimeMillis()))
    }
}
