package cl.hamburgpadel.data.api

import cl.hamburgpadel.data.models.LoginRequest
import cl.hamburgpadel.data.models.LoginResponse
import cl.hamburgpadel.data.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Interface que define los endpoints de la API
 */
interface ApiService {
    
    /**
     * Endpoint para login de usuario
     */
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
    
    /**
     * Endpoint para obtener información del usuario autenticado
     */
    @GET("user/profile")
    suspend fun getUserProfile(@Header("Authorization") token: String): Response<User>
    
    /**
     * Endpoint para logout
     */
    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Void>
    
    /**
     * Endpoint para refrescar token
     */
    @POST("auth/refresh")
    suspend fun refreshToken(@Header("Authorization") token: String): Response<LoginResponse>
}