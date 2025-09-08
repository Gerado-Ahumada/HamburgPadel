package cl.hamburgpadel.data.models

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para el usuario
 */
data class User(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("uuid")
    val uuid: String,
    
    @SerializedName("partidosJugados")
    val partidosJugados: Int = 0,
    
    @SerializedName("createdAt")
    val createdAt: String?
)