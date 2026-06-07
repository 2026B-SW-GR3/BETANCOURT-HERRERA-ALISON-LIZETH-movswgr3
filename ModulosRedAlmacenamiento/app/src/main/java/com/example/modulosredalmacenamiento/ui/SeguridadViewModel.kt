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

    fun guardar() {
        viewModelScope.launch {
            storageManager.guardarDato(llave, valor, mecanismoSeleccionado)
        }
    }

    fun recuperar() {
        viewModelScope.launch {
            val resultado = storageManager.obtenerDato(llave, mecanismoSeleccionado)
            resultadoBusqueda = resultado
            busquedaRealizada = true
        }
    }
}
