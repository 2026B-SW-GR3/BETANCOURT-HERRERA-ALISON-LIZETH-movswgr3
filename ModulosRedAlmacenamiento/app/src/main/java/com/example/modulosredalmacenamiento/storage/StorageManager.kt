package com.example.modulosredalmacenamiento.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

enum class TipoMecanismo {
    SHARED_PREFS, DATASTORE, ENCRYPTED
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_datastore")

class StorageManager(private val context: Context) {

    private val sharedPrefs = context.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs = EncryptedSharedPreferences.create(
        context,
        "encrypted_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    suspend fun guardarDato(llave: String, valor: String, mecanismo: TipoMecanismo) {
        withContext(Dispatchers.IO) {
            when (mecanismo) {
                TipoMecanismo.SHARED_PREFS -> {
                    sharedPrefs.edit().putString(llave, valor).apply()
                }
                TipoMecanismo.DATASTORE -> {
                    val key = stringPreferencesKey(llave)
                    context.dataStore.edit { preferences ->
                        preferences[key] = valor
                    }
                }
                TipoMecanismo.ENCRYPTED -> {
                    encryptedPrefs.edit().putString(llave, valor).apply()
                }
            }
        }
    }

    suspend fun obtenerDato(llave: String, mecanismo: TipoMecanismo): String? {
        return withContext(Dispatchers.IO) {
            when (mecanismo) {
                TipoMecanismo.SHARED_PREFS -> {
                    sharedPrefs.getString(llave, null)
                }
                TipoMecanismo.DATASTORE -> {
                    val key = stringPreferencesKey(llave)
                    context.dataStore.data.map { it[key] }.first()
                }
                TipoMecanismo.ENCRYPTED -> {
                    encryptedPrefs.getString(llave, null)
                }
            }
        }
    }
}
