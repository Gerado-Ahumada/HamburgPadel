package cl.hamburgpadel.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Clase para manejar el almacenamiento seguro de tokens
 */
class TokenManager(private val context: Context) {
    
    companion object {
        private const val PREFS_NAME = "hamburg_padel_secure_prefs"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_UUID = "user_uuid"
    }
    
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    /**
     * Guarda el token de autenticación
     */
    fun saveAuthToken(token: String) {
        sharedPreferences.edit()
            .putString(KEY_AUTH_TOKEN, token)
            .apply()
    }
    
    /**
     * Obtiene el token de autenticación
     */
    fun getAuthToken(): String? {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }
    
    /**
     * Guarda los datos del usuario
     */
    fun saveUserData(email: String, name: String, uuid: String) {
        sharedPreferences.edit()
            .putString(KEY_USER_EMAIL, email)
            .putString(KEY_USER_NAME, name)
            .putString(KEY_USER_UUID, uuid)
            .apply()
    }
    
    /**
     * Obtiene el email del usuario
     */
    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }
    
    /**
     * Obtiene el nombre del usuario
     */
    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_USER_NAME, null)
    }
    
    /**
     * Obtiene el UUID del usuario
     */
    fun getUserUuid(): String? {
        return sharedPreferences.getString(KEY_USER_UUID, null)
    }
    
    /**
     * Verifica si el usuario está autenticado
     */
    fun isLoggedIn(): Boolean {
        return getAuthToken() != null
    }
    
    /**
     * Limpia todos los datos de sesión
     */
    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }
}