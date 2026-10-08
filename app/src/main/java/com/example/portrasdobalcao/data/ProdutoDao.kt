package com.example.portrasdobalcao.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.portrasdobalcao.model.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(produto: Produto): Long

    @Update
    suspend fun atualizar(produto: Produto)

    @Delete
    suspend fun deletar(produto: Produto)

    @Query("SELECT * FROM produtos ORDER BY nome ASC")
    suspend fun listarTodos(): List<Produto>

    /** Usado pelas telas: só produtos ativos, atualizando sozinho a cada mudança no banco. */
    @Query("SELECT * FROM produtos WHERE ativo = 1 ORDER BY nome ASC")
    fun observarTodos(): Flow<List<Produto>>

    @Query("SELECT * FROM produtos WHERE id = :id")
    suspend fun buscarPorId(id: Long): Produto?
}
