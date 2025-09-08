package cl.hamburgpadel.data.models

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para la respuesta del login
 */
data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("token")
    val token: String?,
    
    @SerializedName("user")
    val user: User?
)