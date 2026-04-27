package com.example.rentbetter.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.RegistryConfiguration
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import android.util.Base64

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "secure_rentbetter_prefs")

class SecureStorage(private val context: Context) {

    private val aead: Aead by lazy {
        AeadConfig.register()
        AndroidKeysetManager.Builder()
            .withSharedPref(context, "tink_keyset", "tink_pref_file")
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri("android-keystore://rentbetter_master_key")
            .build()
            .keysetHandle
            .getPrimitive(RegistryConfiguration.get(), Aead::class.java)
    }

    companion object {
        private val KEY_EMAIL = stringPreferencesKey("user_email")
        private val KEY_PASSWORD = stringPreferencesKey("user_password")
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    private fun decrypt(encryptedValue: String?): String? {
        if (encryptedValue == null) return null
        return try {
            val decoded = Base64.decode(encryptedValue, Base64.DEFAULT)
            val decrypted = aead.decrypt(decoded, null)
            String(decrypted)
        } catch (_: Exception) {
            null
        }
    }

    fun getEmail(): String? = runBlocking {
        context.dataStore.data.map { prefs ->
            decrypt(prefs[KEY_EMAIL])
        }.first()
    }
    
    fun getPassword(): String? = runBlocking {
        context.dataStore.data.map { prefs ->
            decrypt(prefs[KEY_PASSWORD])
        }.first()
    }

    fun isLoggedIn(): Boolean = runBlocking {
        context.dataStore.data.map { prefs ->
            prefs[KEY_IS_LOGGED_IN] ?: false
        }.first()
    }
}
