package com.example.myapplication

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)

        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        webView.loadUrl("file:///android_asset/index.html")

        webView.postDelayed({

            updateWebView()

        }, 500)
    }

    private fun updateWebView(){

        val text = getString(R.string.saludo)

        val textColor = colorToHex(getColor(R.color.colorTexto))

        val bgColor = colorToHex(getColor(R.color.colorFondo))

        val js = """
            updateUI(
                '$text',
                '$textColor',
                '$bgColor'
            )
        """.trimIndent()

        webView.evaluateJavascript(js, null)
    }

    private fun colorToHex(color:Int):String{
        return String.format(
            "#%06X",
            0xFFFFFF and color
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        updateWebView()
    }
}