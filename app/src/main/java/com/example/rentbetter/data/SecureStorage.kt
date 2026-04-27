package com.example.rentbetter.data

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureStorage(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_rentbetter_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_EMAIL = "user_email"
        private const val KEY_PASSWORD = "user_password"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun saveCredentials(email: String, password: String) {
        sharedPreferences.edit {
            putString(KEY_EMAIL, email)
            putString(KEY_PASSWORD, password)
        }
    }

    fun getEmail(): String? = sharedPreferences.getString(KEY_EMAIL, null)
    
    fun getPassword(): String? = sharedPreferences.getString(KEY_PASSWORD, null)

    fun setLoggedIn(loggedIn: Boolean) {
        sharedPreferences.edit {
            putBoolean(KEY_IS_LOGGED_IN, loggedIn)
        }
    }

    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)

    fun clear() {
        sharedPreferences.edit { clear() }
    }
}
