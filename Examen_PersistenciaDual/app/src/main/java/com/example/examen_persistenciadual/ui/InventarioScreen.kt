package com.example.examen_persistenciadual.ui

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.examen_persistenciadual.data.NoSqlRepository
import com.example.examen_persistenciadual.data.SqlRepository
import com.example.examen_persistenciadual.data.InventarioDatabase
import com.example.examen_persistenciadual.domain.ProductoRepository
import com.example.examen_persistenciadual.domain.ProductoUI

// ==========================================
// 1. EL CEREBRO DE LA PANTALLA (ViewModel)
// ==========================================
class InventarioViewModel(application: Application) : AndroidViewModel(application) {
    var esSqlActivo by mutableStateOf(true)
    var isLoading by mutableStateOf(false)

    private val database = InventarioDatabase.getDatabase(application)
    private val sqlRepo = SqlRepository(database.productoDao())
    private val noSqlRepo = NoSqlRepository()

    private val repositorioActual: ProductoRepository
        get() = if (esSqlActivo) sqlRepo else noSqlRepo

    var productosMostrar by mutableStateOf<List<ProductoUI>>(emptyList())

    init {
        actualizarLista()
    }

    fun conmutarBaseDatos(valor: Boolean) {
        esSqlActivo = valor
        actualizarLista()
    }

    private fun actualizarLista() {
        viewModelScope.launch {
            // Room access should be off-thread, but for simplicity in this exercise 
            // we are doing a quick refresh. Ideally use Flow.
            productosMostrar = repositorioActual.obtenerProductos()
        }
    }

    fun agregarProducto(nombre: String, fecha: String, activo: Boolean) {
        viewModelScope.launch {
            isLoading = true
            delay(2000) // Simula el tiempo de guardado local

            val nuevo = ProductoUI(
                id = System.currentTimeMillis().toString(),
                nombre = nombre,
                fecha = fecha,
                activo = activo,
                esSql = esSqlActivo
            )

            repositorioActual.guardarProducto(nuevo)
            actualizarLista()
            isLoading = false
        }
    }
}

// ==========================================
// 2. LA INTERFAZ GRÁFICA (Jetpack Compose)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioScreen(viewModel: InventarioViewModel = viewModel()) {
    // Definición de colores pasteles basados en el motor seleccionado
    val colorPrincipal = if (viewModel.esSqlActivo) Color(0xFF81B29A) else Color(0xFFE07A5F)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Persistencia Dual") },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            text = if (viewModel.esSqlActivo) "Modo SQL (Room)" else "Modo NoSQL (Realm)",
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = viewModel.esSqlActivo,
                            onCheckedChange = { viewModel.conmutarBaseDatos(it) }
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {

            var nombre by remember { mutableStateOf("") }

            // CONTENEDOR DEL FORMULARIO
            Card(
                colors = CardDefaults.cardColors(containerColor = colorPrincipal.copy(alpha = 0.1f)),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre del producto") },
                        enabled = !viewModel.isLoading, // Se deshabilita durante la carga
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.agregarProducto(nombre, "2026-05-29", true)
                            nombre = "" // Limpieza del campo
                        },
                        enabled = !viewModel.isLoading && nombre.isNotBlank(), // Validación básica
                        colors = ButtonDefaults.buttonColors(containerColor = colorPrincipal),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Guardar en ${if (viewModel.esSqlActivo) "SQL" else "NoSQL"}")
                        }
                    }
                }
            }

            // LISTA DINÁMICA DE PRODUCTOS
            LazyColumn {
                items(viewModel.productosMostrar) { producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                                Text("Fecha: ${producto.fecha}", style = MaterialTheme.typography.bodySmall)
                            }
                            // Chip indicador del motor de base de datos
                            AssistChip(
                                onClick = { },
                                label = { Text(if (producto.esSql) "SQL" else "NoSQL") },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = if (producto.esSql) Color(0xFF81B29A).copy(alpha = 0.2f) else Color(0xFFE07A5F).copy(alpha = 0.2f)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}