package com.example.taller03_crud

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)

        setContentView(webView)

        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        webView.addJavascriptInterface(
            WebAppInterface(),
            "Android"
        )

        webView.loadUrl(
            "file:///android_asset/index.html"
        )
    }

    inner class WebAppInterface {

        @JavascriptInterface
        fun showToast(message:String){

            runOnUiThread {

                Toast.makeText(
                    this@MainActivity,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        @JavascriptInterface
        fun showDeleteDialog(){

            runOnUiThread {

                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Eliminar")
                    .setMessage("¿Desea eliminar este elemento?")
                    .setPositiveButton("Sí"){ _, _ ->

                        Toast.makeText(
                            this@MainActivity,
                            "Elemento eliminado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        }
    }
}