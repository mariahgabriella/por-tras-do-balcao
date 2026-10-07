package com.seuprojeto.data.entity // TROQUE pelo pacote real do projeto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Registro de estoque de um produto.
 * Relacionamento: cada Produto tem no máximo um Estoque (índice único em produtoId).
 *
 * ATENÇÃO: confira o nome real da classe Produto e da sua chave primária.
 * Se ela não se chamar "id", ajuste parentColumns abaixo.
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
    val produtoId: Int,
    val quantidade: Int = 0,
    val quantidadeMinima: Int = 0
)
