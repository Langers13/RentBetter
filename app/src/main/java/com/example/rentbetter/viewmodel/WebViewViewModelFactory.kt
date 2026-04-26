package com.example.rentbetter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rentbetter.data.SecureStorage

class WebViewViewModelFactory(private val secureStorage: SecureStorage) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WebViewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WebViewViewModel(secureStorage) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
