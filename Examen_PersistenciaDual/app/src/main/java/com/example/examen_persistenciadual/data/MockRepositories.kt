package com.example.examen_persistenciadual.data

import android.util.Log
import com.example.examen_persistenciadual.domain.ProductoRepository
import com.example.examen_persistenciadual.domain.ProductoUI

class SqlRepository(private val dao: ProductoDao) : ProductoRepository {

    private val TAG = "AUDIT_SQL"

    override suspend fun obtenerProductos(): List<ProductoUI> {
        return try {
            val listaEntidades = dao.obtenerTodos()
            Log.i(TAG, "Consulta exitosa: se obtuvieron ${listaEntidades.size} productos")
            listaEntidades.map { entity ->
                ProductoUI(
                    id = entity.id,
                    nombre = entity.nombre,
                    fecha = entity.fecha,
                    activo = entity.activo,
                    esSql = true
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener productos de SQL", e)
            emptyList()
        }
    }

    override suspend fun guardarProducto(producto: ProductoUI) {
        try {
            val entity = ProductoEntity(
                id = producto.id,
                nombre = producto.nombre,
                fecha = producto.fecha,
                activo = producto.activo
            )
            dao.insertar(entity)
            Log.i(TAG, "AUDIT: Se guardó el producto con ID ${producto.id} en SQL")
        } catch (e: Exception) {
            Log.e(TAG, "Error al guardar producto ${producto.id} en SQL", e)
        }
    }

    override suspend fun eliminarProducto(id: String) {
        try {
            dao.eliminar(id)
            Log.i(TAG, "AUDIT: Se eliminó el producto con ID $id de SQL")
        } catch (e: Exception) {
            Log.e(TAG, "Error al eliminar producto $id en SQL", e)
        }
    }
}

class NoSqlRepository : ProductoRepository {
    private val TAG = "AUDIT_NOSQL"
    private var listaNoSql = mutableListOf(
        ProductoUI("100", "Smartphone - NoSQL", "2026-05-21", true, false)
    )

    override suspend fun obtenerProductos(): List<ProductoUI> {
        Log.i(TAG, "Consulta NoSQL exitosa: ${listaNoSql.size} elementos")
        return listaNoSql.toList()
    }

    override suspend fun eliminarProducto(id: String) {
        val removed = listaNoSql.removeAll { it.id == id }
        if (removed) {
            Log.i(TAG, "AUDIT: Se eliminó el producto con ID $id de NoSQL")
        } else {
            Log.w(TAG, "No se encontró el producto con ID $id para eliminar en NoSQL")
        }
    }

    override suspend fun guardarProducto(producto: ProductoUI) {
        listaNoSql.add(producto)
        Log.i(TAG, "AUDIT: Se guardó el producto con ID ${producto.id} en NoSQL")
    }
}