package com.example.examen_persistenciadual.data

import com.example.examen_persistenciadual.domain.ProductoRepository
import com.example.examen_persistenciadual.domain.ProductoUI

class SqlRepository(private val dao: ProductoDao) : ProductoRepository {

    override suspend fun obtenerProductos(): List<ProductoUI> {
        // Trae la lista física de entidades de Room
        val listaEntidades = dao.obtenerTodos()

        // Traduce cada ProductoEntity de la BD a un ProductoUI para la pantalla
        return listaEntidades.map { entity ->
            ProductoUI(
                id = entity.id,
                nombre = entity.nombre,
                fecha = entity.fecha,
                activo = entity.activo,
                esSql = true
            )
        }
    }

    // Usamos 'suspend' porque la BD requiere procesamiento asíncrono para no congelar la pantalla
    override suspend fun guardarProducto(producto: ProductoUI) {
        val entity = ProductoEntity(
            id = producto.id,
            nombre = producto.nombre,
            fecha = producto.fecha,
            activo = producto.activo
        )
        dao.insertar(entity)
    }

    override suspend fun eliminarProducto(id: String) {
        dao.eliminar(id)
    }
}

class NoSqlRepository : ProductoRepository {
    private var listaNoSql = mutableListOf(
        ProductoUI("100", "Smartphone - NoSQL", "2026-05-21", true, false)
    )
    override suspend fun obtenerProductos() = listaNoSql
    override suspend fun eliminarProducto(id: String) { listaNoSql.removeAll { it.id == id } }
    override suspend fun guardarProducto(producto: ProductoUI) { listaNoSql.add(producto) }
}