package com.example.portrasdobalcao.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.portrasdobalcao.model.Estoque
import com.example.portrasdobalcao.model.Produto
import com.example.portrasdobalcao.model.Usuario
import com.example.portrasdobalcao.model.UsuarioDao

@Database(
    entities = [Produto::class, Usuario::class, Estoque::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun produtoDao(): ProdutoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun estoqueDao(): EstoqueDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instancia ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "por_tras_do_balcao.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instancia = it }
            }
    }
}