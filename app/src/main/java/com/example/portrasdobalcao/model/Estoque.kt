package com.example.portrasdobalcao.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Registro de estoque de um produto.
 * Relacionamento: cada Produto tem no máximo um Estoque (índice único em produtoId).
 */
@Entity(
    tableName = "estoque",
    foreignKeys = [
        ForeignKey(
            entity = Produto::class,
            parentColumns = ["id"],
            childColumns = ["produtoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["produtoId"], unique = true)]
)
data class Estoque(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val produtoId: Long,
    val quantidade: Int = 0,
    val quantidadeMinima: Int = 0
)
