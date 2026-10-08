package com.example.portrasdobalcao.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

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
    indices = [Index("produtoId")]
)
data class Estoque(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val produtoId: Long,
    val quantidade: Int,
    val quantidadeMinima: Int = 0,
    val ultimaAtualizacao: Long = System.currentTimeMillis()
)
