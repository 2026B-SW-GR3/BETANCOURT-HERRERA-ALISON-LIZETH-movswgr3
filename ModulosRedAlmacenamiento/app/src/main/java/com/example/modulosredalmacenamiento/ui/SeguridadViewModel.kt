package com.example.modulosredalmacenamiento.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modulosredalmacenamiento.storage.StorageManager
import com.example.modulosredalmacenamiento.storage.TipoMecanismo
import kotlinx.coroutines.launch

class SeguridadViewModel(application: Application) : AndroidViewModel(application) {
    private val storageManager = StorageManager(application)

    var llave by mutableStateOf("")
    var valor by mutableStateOf("")
    var mecanismoSeleccionado by mutableStateOf(TipoMecanismo.SHARED_PREFS)
    var resultadoBusqueda by mutableStateOf<String?>(null)
    var busquedaRealizada by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var operacionExitosa by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun guardar() {
        if (llave.isBlank()) {
            errorMessage = "La llave no puede estar vacía"
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                storageManager.guardarDato(llave, valor, mecanismoSeleccionado)
                operacionExitosa = true
            } catch (e: Exception) {
                errorMessage = "Error al guardar: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun recuperar() {
        if (llave.isBlank()) {
            errorMessage = "La llave no puede estar vacía"
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val resultado = storageManager.obtenerDato(llave, mecanismoSeleccionado)
                resultadoBusqueda = resultado
                busquedaRealizada = true
            } catch (e: Exception) {
                errorMessage = "Error al recuperar: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun resetSuccess() {
        operacionExitosa = false
    }
}
