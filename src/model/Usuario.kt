package com.example.portrasdobalcao.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuario",
    indices = [Index(value = ["nomeLoja"], unique = true)]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val nomeLoja: String,
    val nomeGestor: String,
    val categoriaLoja: String,
    val senha: String,
    val fotoPerfilPath: String? = null
)
