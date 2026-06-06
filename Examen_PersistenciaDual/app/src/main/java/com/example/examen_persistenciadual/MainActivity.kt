package com.example.examen_persistenciadual

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.examen_persistenciadual.ui.InventarioScreen
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Llamar directamente a la pantalla principal
            InventarioScreen()
        }
    }
}