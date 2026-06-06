package com.example.examen_persistenciadual.domain // (Asegúrate de que coincida con tu paquete real)

// El modelo visual
data class ProductoUI(
    val id: String,
    val nombre: String,
    val fecha: String,
    val activo: Boolean,
    val esSql: Boolean
)

// La interfaz estandarizada
interface ProductoRepository {
    suspend fun obtenerProductos(): List<ProductoUI>
    suspend fun guardarProducto(producto: ProductoUI)
    suspend fun eliminarProducto(id: String)
}