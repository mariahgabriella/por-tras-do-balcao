package com.example.portrasdobalcao.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fornecedores")
data class Fornecedor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val telefone: String,
    val categoriaFornecida: String,
    val ativo: Boolean = true
) {
    init {
        require(nome.isNotBlank()) { "O nome do fornecedor não pode ficar em branco." }
        require(telefone.isNotBlank()) { "O telefone do fornecedor não pode ficar em branco." }
        require(categoriaFornecida.isNotBlank()) { "A categoria fornecida não pode ficar em branco." }
    }
}
