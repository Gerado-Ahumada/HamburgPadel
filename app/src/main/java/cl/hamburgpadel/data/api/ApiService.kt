package cl.hamburgpadel.data.api

import cl.hamburgpadel.data.models.LoginRequest
import cl.hamburgpadel.data.models.LoginResponse
import cl.hamburgpadel.data.models.User
import cl.hamburgpadel.data.models.PlayersResponse
import cl.hamburgpadel.data.model.SignupRequest
import cl.hamburgpadel.data.model.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

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
    
    /**
     * Endpoint para registrar nuevo usuario
     */
    @POST("auth/signup")
    suspend fun signup(
        @Header("Authorization") token: String,
        @Body signupRequest: SignupRequest
    ): Response<SignupResponse>
    
    /**
     * Endpoint para obtener lista de usuarios/jugadores
     */
    @GET("users/players")
    suspend fun getUsersList(
        @Header("Authorization") token: String,
        @Query("name") name: String? = null,
        @Query("category") category: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<PlayersResponse>
}