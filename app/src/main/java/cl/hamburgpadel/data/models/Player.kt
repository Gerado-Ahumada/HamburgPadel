package cl.hamburgpadel.data.models

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para un jugador/usuario
 */
data class Player(
    @SerializedName("username")
    val username: String,
    
    @SerializedName("fullName")
    val fullName: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("phone")
    val phone: String,
    
    @SerializedName("playerCategory")
    val playerCategory: String,
    
    @SerializedName("status")
    val status: String,
    
    @SerializedName("uuid")
    val uuid: String
)