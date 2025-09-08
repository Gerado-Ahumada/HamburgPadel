package cl.hamburgpadel.data.models

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para la respuesta del login
 */
data class LoginResponse(
    @SerializedName("token")
    val token: String?,
    
    @SerializedName("tipo")
    val tipo: String?,
    
    @SerializedName("id")
    val id: Int?,
    
    @SerializedName("username")
    val username: String?,
    
    @SerializedName("email")
    val email: String?,
    
    @SerializedName("role")
    val role: String?
) {
    // Propiedades de conveniencia para mantener compatibilidad
    val success: Boolean get() = token != null
    val message: String? get() = null
    val user: User? get() = if (id != null && username != null) {
        User(
            id = id.toString(),
            email = email ?: "",
            name = username ?: "",
            uuid = id.toString(),
            partidosJugados = 0,
            createdAt = null
        )
    } else null
}