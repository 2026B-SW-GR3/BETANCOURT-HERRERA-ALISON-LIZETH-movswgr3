package com.example.ciclopersistencia

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val TAG = "CicloDeVidaLog"
    private var count = 0
    private lateinit var tvCounter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Activity fue creada")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tvCounter = findViewById(R.id.tvCounter)
        val btnIncrement = findViewById<Button>(R.id.btnIncrement)
        val btnDecrement = findViewById<Button>(R.id.btnDecrement)
        val btnReset = findViewById<Button>(R.id.btnReset)

        // Recuperar el estado si la actividad fue recreada (ej. rotación de pantalla)
        if (savedInstanceState != null) {
            count = savedInstanceState.getInt("contador_key", 0)
            tvCounter.text = count.toString()
            Log.d(TAG, "onCreate: Estado recuperado. Contador = $count")
        }

        btnIncrement.setOnClickListener {
            count++
            tvCounter.text = count.toString()
        }

        btnDecrement.setOnClickListener {
            count--
            tvCounter.text = count.toString()
        }

        btnReset.setOnClickListener {
            count = 0
            tvCounter.text = count.toString()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Activity visible pero no interactiva aún")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Activity en primer plano e interactiva")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Activity perdiendo el foco (ej. abriendo otra app o rotando)")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Activity ya no es visible")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Activity a punto de ser destruida")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: Activity regresando a ser visible desde estado Stop")
    }

    // Persistencia del estado
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("contador_key", count)
        Log.d(TAG, "onSaveInstanceState: Guardando estado. Contador = $count")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // También se puede recuperar aquí en lugar de onCreate
        Log.d(TAG, "onRestoreInstanceState: Restaurando estado")
    }
}