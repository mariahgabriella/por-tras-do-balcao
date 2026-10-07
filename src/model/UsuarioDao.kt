package com.example.portrasdobalcao.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun inserir(usuario: Usuario): Long

    @Update
    suspend fun atualizar(usuario: Usuario): Int

    @Query("SELECT * FROM usuario LIMIT 1")
    suspend fun buscarPrimeiroUsuario(): Usuario?

    @Query("SELECT * FROM usuario WHERE nomeLoja = :nomeLoja LIMIT 1")
    suspend fun buscarPorNomeLoja(nomeLoja: String): Usuario?

    @Query("SELECT * FROM usuario WHERE id = :id")
    suspend fun buscarPorId(id: Long): Usuario?

    @Query("SELECT * FROM usuario WHERE id = :id")
    fun observarPorId(id: Long): Flow<Usuario?>
}
