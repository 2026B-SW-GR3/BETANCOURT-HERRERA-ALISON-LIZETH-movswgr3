package com.example.examen_persistenciadual.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos_tabla")
    suspend fun obtenerTodos(): List<ProductoEntity>

    // AGREGAMOS 'suspend' AQUÍ:
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: ProductoEntity)

    @Query("DELETE FROM productos_tabla WHERE id = :productoId")
    suspend fun eliminar(productoId: String)
}