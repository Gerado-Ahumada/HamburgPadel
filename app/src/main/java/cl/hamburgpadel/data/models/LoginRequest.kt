package cl.hamburgpadel.data.models

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para la solicitud de login
 */
data class LoginRequest(
    @SerializedName("username")
    val username: String,
    
    @SerializedName("password")
    val password: String
)