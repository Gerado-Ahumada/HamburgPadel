package cl.hamburgpadel.data.repository

import cl.hamburgpadel.data.models.LoginRequest
import cl.hamburgpadel.data.models.LoginResponse
import cl.hamburgpadel.data.models.User
import cl.hamburgpadel.data.models.PlayersResponse
import cl.hamburgpadel.data.model.SignupRequest
import cl.hamburgpadel.data.model.SignupResponse
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
    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val loginRequest = LoginRequest(username, password)
            val response = apiService.login(loginRequest)
            
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    Result.success(loginResponse)
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
    
    /**
     * Registra un nuevo usuario (requiere token de admin)
     */
    suspend fun signup(adminToken: String, signupRequest: SignupRequest): Result<SignupResponse> {
        return try {
            val response = apiService.signup("Bearer $adminToken", signupRequest)
            
            if (response.isSuccessful) {
                response.body()?.let { signupResponse ->
                    Result.success(signupResponse)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception("Error HTTP: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene la lista de usuarios/jugadores con filtros y paginación
     */
    suspend fun getUsersList(
        token: String,
        name: String? = null,
        category: String? = null,
        page: Int = 0,
        size: Int = 10
    ): Result<PlayersResponse> {
        return try {
            val response = apiService.getUsersList(
                token = "Bearer $token",
                name = name,
                category = category,
                page = page,
                size = size
            )
            
            if (response.isSuccessful) {
                response.body()?.let { playersResponse ->
                    Result.success(playersResponse)
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception("Error HTTP: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}