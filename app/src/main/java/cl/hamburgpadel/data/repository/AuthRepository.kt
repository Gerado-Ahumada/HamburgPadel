package cl.hamburgpadel.data.repository

import cl.hamburgpadel.data.models.LoginRequest
import cl.hamburgpadel.data.models.LoginResponse
import cl.hamburgpadel.data.models.User
import cl.hamburgpadel.data.network.NetworkManager
import retrofit2.Response

/**
 * Repository que maneja la lógica de autenticación
 */
class AuthRepository {
    
    private val apiService = NetworkManager.apiService
    
    /**
     * Realiza el login del usuario
     */
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val loginRequest = LoginRequest(email, password)
            val response = apiService.login(loginRequest)
            
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    if (loginResponse.success) {
                        Result.success(loginResponse)
                    } else {
                        Result.failure(Exception(loginResponse.message ?: "Error de autenticación"))
                    }
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception("Error HTTP: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene el perfil del usuario autenticado
     */
    suspend fun getUserProfile(token: String): Result<User> {
        return try {
            val response = apiService.getUserProfile("Bearer $token")
            
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    Result.success(user)
                } ?: Result.failure(Exception("Perfil de usuario no encontrado"))
            } else {
                Result.failure(Exception("Error HTTP: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Realiza el logout del usuario
     */
    suspend fun logout(token: String): Result<Unit> {
        return try {
            val response = apiService.logout("Bearer $token")
            
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al cerrar sesión: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}