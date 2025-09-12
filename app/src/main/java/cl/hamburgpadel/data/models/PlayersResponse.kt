package cl.hamburgpadel.data.models

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para la respuesta del API de usuarios/jugadores
 */
data class PlayersResponse(
    @SerializedName("players")
    val players: List<Player>,
    
    @SerializedName("currentPage")
    val currentPage: Int,
    
    @SerializedName("totalPages")
    val totalPages: Int,
    
    @SerializedName("totalElements")
    val totalElements: Int,
    
    @SerializedName("size")
    val size: Int,
    
    @SerializedName("hasNext")
    val hasNext: Boolean,
    
    @SerializedName("hasPrevious")
    val hasPrevious: Boolean
)