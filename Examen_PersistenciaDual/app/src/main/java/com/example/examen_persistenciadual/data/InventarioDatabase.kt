package com.example.examen_persistenciadual.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Le decimos a Room qué entidades (tablas) usar y la versión de la BD
@Database(entities = [ProductoEntity::class], version = 1, exportSchema = false)
abstract class InventarioDatabase : RoomDatabase() {

    // Conectamos el DAO que creamos en el paso anterior
    abstract fun productoDao(): ProductoDao

    companion object {
        @Volatile
        private var INSTANCE: InventarioDatabase? = null

        fun getDatabase(context: Context): InventarioDatabase {
            return INSTANCE ?: synchronized(this) {
                // Si la BD no existe, la crea con este nombre
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InventarioDatabase::class.java,
                    "inventario_sql_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}