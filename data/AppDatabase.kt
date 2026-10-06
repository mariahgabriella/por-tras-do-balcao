package com.example.portrasdobalcao.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.portrasdobalcao.model.Produto

@Database(
    entities = [Produto::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao
}
