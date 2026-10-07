package com.example.portrasdobalcao.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.portrasdobalcao.model.Produto

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

    @Query("SELECT * FROM produtos WHERE id = :id")
    suspend fun buscarPorId(id: Long): Produto?
}
