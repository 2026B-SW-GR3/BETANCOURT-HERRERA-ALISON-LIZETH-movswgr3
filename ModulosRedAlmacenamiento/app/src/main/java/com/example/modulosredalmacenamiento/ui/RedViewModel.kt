package com.example.modulosredalmacenamiento.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modulosredalmacenamiento.network.Post
import com.example.modulosredalmacenamiento.network.RetrofitClient
import kotlinx.coroutines.launch

class RedViewModel : ViewModel() {

    var idInput by mutableStateOf("")
    var post by mutableStateOf<Post?>(null)
    var titleInput by mutableStateOf("")
    var bodyInput by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var updateSuccess by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun getPost() {
        val id = idInput.toIntOrNull() ?: run {
            errorMessage = "Por favor ingresa un ID válido"
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val result = RetrofitClient.apiService.getPost(id)
                post = result
                titleInput = result.title
                bodyInput = result.body
            } catch (e: Exception) {
                errorMessage = "Error al obtener el post: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updatePost() {
        val currentPost = post ?: return
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val updatedPost = currentPost.copy(title = titleInput, body = bodyInput)
                val result = RetrofitClient.apiService.updatePost(updatedPost.id, updatedPost)
                post = result
                updateSuccess = true
            } catch (e: Exception) {
                errorMessage = "Error al actualizar: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun resetUpdateSuccess() {
        updateSuccess = false
    }
}
