package com.example.rentbetter.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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

    var isMenuVisible by mutableStateOf(value = false)
        private set

    var refreshTrigger by mutableIntStateOf(value = 0)
        private set

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        appState = if (secureStorage.isLoggedIn() && (secureStorage.getEmail() != null)) {
            AppState.LoggedIn
        } else {
            AppState.LoggedOut
        }
    }

    fun setMenuVisibility(visible: Boolean) {
        isMenuVisible = visible
    }
}
