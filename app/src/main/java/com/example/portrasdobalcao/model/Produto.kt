package com.example.portrasdobalcao.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produtos")
data class Produto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val categoria: String,
    val precoCusto: Double,
    val quantidadeEstoque: Int,
    val ativo: Boolean = true
) {
    init {
        require(nome.isNotBlank()) { "O nome do produto não pode ficar em branco." }
        require(categoria.isNotBlank()) { "A categoria do produto não pode ficar em branco." }
        require(precoCusto > 0.0) { "O preço de custo deve ser maior que zero." }
        require(quantidadeEstoque >= 0) { "A quantidade em estoque não pode ser negativa." }
    }
}
