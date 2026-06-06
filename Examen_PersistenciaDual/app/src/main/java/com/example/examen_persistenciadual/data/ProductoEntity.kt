package com.example.examen_persistenciadual.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos_tabla")
data class ProductoEntity(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val fecha: String,
    val activo: Boolean
)