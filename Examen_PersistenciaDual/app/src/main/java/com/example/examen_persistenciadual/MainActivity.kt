package com.example.examen_persistenciadual

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.examen_persistenciadual.ui.InventarioScreen
import com.example.examen_persistenciadual.ui.theme.Examen_PersistenciaDualTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Examen_PersistenciaDualTheme {
                // Llamar directamente a la pantalla principal envuelta en el tema
                InventarioScreen()
            }
        }
    }
}