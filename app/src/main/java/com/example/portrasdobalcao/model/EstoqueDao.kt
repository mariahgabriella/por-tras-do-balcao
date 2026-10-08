package com.example.portrasdobalcao.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EstoqueDao {

    @Insert
    suspend fun inserir(estoque: Estoque)

    @Update
    suspend fun atualizar(estoque: Estoque)

    @Query("SELECT * FROM estoque")
    fun listarTodos(): Flow<List<Estoque>>

    @Query("SELECT * FROM estoque WHERE produtoId = :produtoId")
    suspend fun buscarPorProduto(produtoId: Long): Estoque?
}
