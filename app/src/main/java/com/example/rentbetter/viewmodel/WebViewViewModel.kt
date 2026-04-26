package com.example.rentbetter.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.rentbetter.data.SecureStorage

sealed class AppState {
    object Loading : AppState()
    object LoggedIn : AppState()
    object LoggedOut : AppState()
}

class WebViewViewModel(private val secureStorage: SecureStorage) : ViewModel() {

    var appState by mutableStateOf<AppState>(AppState.Loading)
        private set

    var isMenuVisible by mutableStateOf(false)
        private set

    var refreshTrigger by mutableStateOf(0)
        private set

    init {
        checkLoginStatus()
    }

    fun refresh() {
        refreshTrigger++
    }

    private fun checkLoginStatus() {
        appState = if (secureStorage.isLoggedIn() && secureStorage.getEmail() != null) {
            AppState.LoggedIn
        } else {
            AppState.LoggedOut
        }
    }

    fun saveLogin(email: String, password: String) {
        secureStorage.saveCredentials(email, password)
        secureStorage.setLoggedIn(true)
        appState = AppState.LoggedIn
    }

    fun logout() {
        secureStorage.clear()
        appState = AppState.LoggedOut
    }

    fun toggleMenu() {
        isMenuVisible = !isMenuVisible
    }

    fun setMenuVisibility(visible: Boolean) {
        isMenuVisible = visible
    }
}
