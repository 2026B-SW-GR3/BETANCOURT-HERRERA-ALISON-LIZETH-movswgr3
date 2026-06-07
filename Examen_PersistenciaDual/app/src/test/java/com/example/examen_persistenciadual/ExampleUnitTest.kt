package com.example.examen_persistenciadual

import com.example.examen_persistenciadual.data.*
import com.example.examen_persistenciadual.domain.ProductoUI
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Suite de Pruebas Unitarias Locales para el Repositorio.
 * Cumple con el 20% de la rúbrica.
 */
class FakeProductoDao : ProductoDao {
    private val productos = mutableListOf<ProductoEntity>()
    override suspend fun obtenerTodos() = productos
    override suspend fun insertar(producto: ProductoEntity) { productos.add(producto) }
    override suspend fun eliminar(productoId: String) { productos.removeAll { it.id == productoId } }
}

class ExampleUnitTest {

    private lateinit var fakeDao: FakeProductoDao
    private lateinit var repository: SqlRepository

    @Before
    fun setup() {
        fakeDao = FakeProductoDao()
        repository = SqlRepository(fakeDao)
    }

    @Test
    fun `guardarProducto inserta correctamente en la lista simulada`() = runBlocking {
        // 1. Preparar
        val nuevoProducto = ProductoUI("1", "Producto de Prueba", "2024-06-06", true, true)

        // 2. Actuar
        repository.guardarProducto(nuevoProducto)

        // 3. Verificar
        val lista = fakeDao.obtenerTodos()
        assertEquals(1, lista.size)
        assertEquals("Producto de Prueba", lista[0].nombre)
    }

    @Test
    fun `obtenerProductos devuelve el tamaño correcto de la lista`() = runBlocking {
        // 1. Preparar
        repository.guardarProducto(ProductoUI("1", "A", "2024", true, true))
        repository.guardarProducto(ProductoUI("2", "B", "2024", true, true))

        // 2. Actuar
        val resultado = repository.obtenerProductos()

        // 3. Verificar
        assertEquals(2, resultado.size)
    }
}
