package com.example.modulosredalmacenamiento.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modulosredalmacenamiento.storage.TipoMecanismo

@Composable
fun SeguridadScreen(viewModel: SeguridadViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mostrar error si existe
        viewModel.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }

        OutlinedTextField(
            value = viewModel.llave,
            onValueChange = { viewModel.llave = it },
            label = { Text("Llave") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        )

        OutlinedTextField(
            value = viewModel.valor,
            onValueChange = { viewModel.valor = it },
            label = { Text("Valor") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        )

        Text("Mecanismo de Persistencia:", style = MaterialTheme.typography.titleMedium)

        Column(Modifier.selectableGroup()) {
            TipoMecanismo.values().forEach { mecanismo ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .selectable(
                            selected = (mecanismo == viewModel.mecanismoSeleccionado),
                            onClick = { if (!viewModel.isLoading) viewModel.mecanismoSeleccionado = mecanismo },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (mecanismo == viewModel.mecanismoSeleccionado),
                        onClick = null,
                        enabled = !viewModel.isLoading
                    )
                    Text(
                        text = mecanismo.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp),
                        color = if (viewModel.isLoading) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f) else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.guardar() },
                modifier = Modifier.weight(1f),
                enabled = !viewModel.isLoading
            ) {
                Text("Guardar")
            }
            Button(
                onClick = { viewModel.recuperar() },
                modifier = Modifier.weight(1f),
                enabled = !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Recuperar")
                }
            }
        }

        if (viewModel.busquedaRealizada) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Resultado:", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = viewModel.resultadoBusqueda ?: "el secreto no fue encontrado",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
