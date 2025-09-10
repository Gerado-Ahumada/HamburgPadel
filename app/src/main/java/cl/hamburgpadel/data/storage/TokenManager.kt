package cl.hamburgpadel.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import cl.hamburgpadel.data.models.UserRole

/**
 * Clase para manejar el almacenamiento seguro de tokens
 */
class TokenManager(private val context: Context) {
    
    companion object {
        private const val PREFS_NAME = "hamburg_padel_secure_prefs"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USERNAME = "username"
        private const val KEY_USER_UUID = "user_uuid"
        private const val KEY_USER_ROLE = "user_role"
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
     * Guarda los datos completos del usuario desde el login
     */
    fun saveCompleteUserData(id: Int?, username: String?, email: String?, role: String?) {
        sharedPreferences.edit()
            .putInt(KEY_USER_ID, id ?: 0)
            .putString(KEY_USERNAME, username)
            .putString(KEY_USER_EMAIL, email)
            .putString(KEY_USER_ROLE, role)
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
     * Obtiene el ID del usuario
     */
    fun getUserId(): Int {
        return sharedPreferences.getInt(KEY_USER_ID, 0)
    }
    
    /**
     * Obtiene el username del usuario
     */
    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }
    
    /**
     * Obtiene el rol del usuario como string
     */
    fun getUserRoleString(): String? {
        return sharedPreferences.getString(KEY_USER_ROLE, null)
    }
    
    /**
     * Obtiene el rol del usuario como enum
     */
    fun getUserRole(): UserRole? {
        val roleString = getUserRoleString()
        return UserRole.fromString(roleString)
    }
    
    /**
     * Verifica si el usuario es un administrador
     */
    fun isAdmin(): Boolean {
        return getUserRole()?.isAdmin() == true
    }
    
    /**
     * Verifica si el usuario es un jugador
     */
    fun isPlayer(): Boolean {
        return getUserRole()?.isPlayer() == true
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