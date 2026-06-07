package com.example.examen_persistenciadual.ui

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.example.examen_persistenciadual.R
import com.example.examen_persistenciadual.data.NoSqlRepository
import com.example.examen_persistenciadual.data.SqlRepository
import com.example.examen_persistenciadual.data.InventarioDatabase
import com.example.examen_persistenciadual.domain.ProductoRepository
import com.example.examen_persistenciadual.domain.ProductoUI

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
            val lista = repositorioActual.obtenerProductos()
            productosMostrar = ArrayList(lista)
        }
    }

    fun agregarProducto(nombre: String, fecha: String, activo: Boolean) {
        viewModelScope.launch {
            isLoading = true
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

    fun borrarProducto(id: String) {
        viewModelScope.launch {
            repositorioActual.eliminarProducto(id)
            actualizarLista()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioScreen(viewModel: InventarioViewModel = viewModel()) {
    // Colores obtenidos desde colors.xml
    val colorSql = colorResource(id = R.color.sql_primary)
    val colorNoSql = colorResource(id = R.color.nosql_primary)
    val colorPrincipal = if (viewModel.esSqlActivo) colorSql else colorNoSql

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_main)) },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            text = if (viewModel.esSqlActivo) stringResource(R.string.mode_sql) else stringResource(R.string.mode_nosql),
                            fontSize = 10.sp,
                            lineHeight = 12.sp,
                            modifier = Modifier.width(100.dp)
                        )
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

            Card(
                colors = CardDefaults.cardColors(containerColor = colorPrincipal.copy(alpha = 0.1f)),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text(stringResource(R.string.label_producto)) },
                        enabled = !viewModel.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.agregarProducto(nombre, "2026-05-29", true)
                            nombre = ""
                        },
                        enabled = !viewModel.isLoading && nombre.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = colorPrincipal),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Text(if (viewModel.esSqlActivo) stringResource(R.string.btn_guardar_sql) else stringResource(R.string.btn_guardar_nosql))
                        }
                    }
                }
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(viewModel.productosMostrar, key = { it.id }) { producto ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                                Text(stringResource(R.string.fecha_placeholder, producto.fecha), style = MaterialTheme.typography.bodySmall)
                            }
                            IconButton(onClick = { viewModel.borrarProducto(producto.id) }) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Color.Red
                                )
                            }
                            AssistChip(
                                onClick = { },
                                label = { Text(if (producto.esSql) stringResource(R.string.tag_sql) else stringResource(R.string.tag_nosql)) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = (if (producto.esSql) colorSql else colorNoSql).copy(alpha = 0.2f)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
