package com.example.modulosredalmacenamiento.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RedScreen(viewModel: RedViewModel = viewModel()) {
    val context = LocalContext.current

    LaunchedEffect(viewModel.updateSuccess) {
        if (viewModel.updateSuccess) {
            Toast.makeText(context, "Actualización exitosa (200 OK)", Toast.LENGTH_SHORT).show()
            viewModel.resetUpdateSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Sección GET
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = viewModel.idInput,
                onValueChange = { viewModel.idInput = it },
                label = { Text("ID del Post") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = !viewModel.isLoading
            )
            Button(
                onClick = { viewModel.getPost() },
                enabled = !viewModel.isLoading
            ) {
                Text("Obtener")
            }
        }

        Divider()

        // Formulario PUT
        OutlinedTextField(
            value = viewModel.titleInput,
            onValueChange = { viewModel.titleInput = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        )

        OutlinedTextField(
            value = viewModel.bodyInput,
            onValueChange = { viewModel.bodyInput = it },
            label = { Text("Cuerpo") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading,
            minLines = 3
        )

        Button(
            onClick = { viewModel.updatePost() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading && viewModel.post != null
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Actualizar")
            }
        }
    }
}
