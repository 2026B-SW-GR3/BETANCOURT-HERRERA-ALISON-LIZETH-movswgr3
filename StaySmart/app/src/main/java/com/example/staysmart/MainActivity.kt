package com.example.staysmart

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Encontrar las vistas
        val mapWebView = findViewById<WebView>(R.id.mapWebView)
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val tvUser = findViewById<TextView>(R.id.tvUser)
        val btnNextApp = findViewById<Button>(R.id.btnNextApp)

        // 1. Recibir los datos del Intent de CityDrive (App 2) según su código real
        val pasajeroId = intent?.getStringExtra("PARAM_HUESPED_ID") ?: "Usuario VIP"
        val idReserva = intent?.getStringExtra("PARAM_ID_RESERVA") ?: "N/A"
        
        // Nombre genérico del hotel para evitar complicaciones
        val hotelNombre = "Smart Hotel"
        
        // Coordenadas base (Fallback si CityDrive no las envía)
        var hotelLat = -0.1691 
        var hotelLng = -78.4851

        // Extraer las coordenadas exactas con las llaves que él usó
        if (intent?.hasExtra("PARAM_DESTINO_LAT") == true) {
            val lat = intent?.extras?.get("PARAM_DESTINO_LAT")
            if (lat is Double) hotelLat = lat
            else if (lat is Float) hotelLat = lat.toDouble()
        }
        if (intent?.hasExtra("PARAM_DESTINO_LNG") == true) {
            val lng = intent?.extras?.get("PARAM_DESTINO_LNG")
            if (lng is Double) hotelLng = lng
            else if (lng is Float) hotelLng = lng.toDouble()
        }

        // Extraer el tiempo de llegada que envía CityDrive
        val timestamp = intent?.getLongExtra("PARAM_TIMESTAMP_LLEGADA", System.currentTimeMillis()) ?: System.currentTimeMillis()
        val formatoHora = java.text.SimpleDateFormat("HH:mm - dd MMM yyyy", java.util.Locale.getDefault())
        val horaLlegada = formatoHora.format(java.util.Date(timestamp))

        // Encontrar tvTime
        val tvTime = findViewById<TextView>(R.id.tvTime)

        // Actualizar UI con los datos
        tvWelcome.text = "Check-In: $hotelNombre"
        tvUser.text = "Huésped: $pasajeroId (Reserva: $idReserva)"
        tvTime.text = "Llegada: $horaLlegada"

        // 2. Configurar el Mapa Real (Leaflet OpenStreetMap)
        mapWebView.settings.javaScriptEnabled = true
        mapWebView.settings.domStorageEnabled = true
        
        // Generamos los puntos de interés dinámicamente basados en la ubicación del hotel
        val htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
                <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
                <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                <style>
                    body { padding: 0; margin: 0; background-color: #f5f5f5; }
                    html, body, #map { height: 100%; width: 100vw; }
                    .leaflet-popup-content-wrapper { border-radius: 8px; }
                </style>
            </head>
            <body>
                <div id="map"></div>
                <script>
                    // Inicializar el mapa centrado en el destino donde te dejó el taxi
                    var map = L.map('map').setView([$hotelLat, $hotelLng], 16);
                    
                    // Capa de mapa real de OpenStreetMap
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        maxZoom: 19,
                        attribution: '© OpenStreetMap'
                    }).addTo(map);
                    
                    // Pin 1: El Hotel (Destino de CityDrive)
                    var hotelIcon = L.icon({
                        iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
                        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
                        iconSize: [25, 41],
                        iconAnchor: [12, 41],
                        popupAnchor: [1, -34],
                        shadowSize: [41, 41]
                    });
                    
                    var hotelMarker = L.marker([$hotelLat, $hotelLng], {icon: hotelIcon}).addTo(map);
                    hotelMarker.bindPopup('<b style="color:red; font-size:14px;">🏨 $hotelNombre</b><br>Tu alojamiento actual.').openPopup();

                    // Pin 2: Recomendación - Cajero / Banco Cercano
                    var bankIcon = L.icon({
                        iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-blue.png',
                        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
                        iconSize: [25, 41], iconAnchor: [12, 41], popupAnchor: [1, -34]
                    });
                    L.marker([$hotelLat + 0.0015, $hotelLng - 0.001], {icon: bankIcon}).addTo(map)
                        .bindPopup('<b>🏧 Cajero Automático 24h</b><br>A 2 minutos caminando');
                        
                    // Pin 3: Recomendación - Farmacia
                    var pharmaIcon = L.icon({
                        iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
                        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
                        iconSize: [25, 41], iconAnchor: [12, 41], popupAnchor: [1, -34]
                    });
                    L.marker([$hotelLat - 0.001, $hotelLng + 0.002], {icon: pharmaIcon}).addTo(map)
                        .bindPopup('<b>⚕️ Farmacia Cruz Azul</b><br>A 3 minutos caminando');
                        
                    // Pin 4: Transporte Público / Metro
                    var metroIcon = L.icon({
                        iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-violet.png',
                        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
                        iconSize: [25, 41], iconAnchor: [12, 41], popupAnchor: [1, -34]
                    });
                    L.marker([$hotelLat + 0.002, $hotelLng + 0.0015], {icon: metroIcon}).addTo(map)
                        .bindPopup('<b>🚇 Parada de Metro/Buses</b><br>Conexión principal de la ciudad');
                </script>
            </body>
            </html>
        """.trimIndent()

        mapWebView.loadDataWithBaseURL("https://localhost/", htmlContent, "text/html", "UTF-8", null)

        // 3. Configurar el salto a la App 4 (ArenaTick)
        btnNextApp.setOnClickListener {
            val intentNextApp = Intent(Intent.ACTION_VIEW, Uri.parse("arenatick://event_catalog"))

            intentNextApp.putExtra("PARAM_PERFIL_INTERESES", "sports,concerts")
            intentNextApp.putExtra("PARAM_HOTEL_LAT", hotelLat)
            intentNextApp.putExtra("PARAM_HOTEL_LNG", hotelLng)
            intentNextApp.putExtra("PARAM_CODIGO_PROMO", "PROMO-HOTEL-STADIUM")

            try {
                startActivity(intentNextApp)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this, 
                    "¡La app ArenaTick (App 4) no está instalada!", 
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}