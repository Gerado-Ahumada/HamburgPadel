package cl.hamburgpadel.data.models

import com.google.gson.annotations.SerializedName

/**
 * Modelo genérico para respuestas de la API
 */
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: T?
)

/**
 * Modelo para errores de la API
 */
data class ApiError(
    @SerializedName("error")
    val error: String,
    
    @SerializedName("code")
    val code: Int?
)