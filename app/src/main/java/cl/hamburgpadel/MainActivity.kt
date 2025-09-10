package cl.hamburgpadel

import android.content.Intent
import android.os.Bundle

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cl.hamburgpadel.data.repository.AuthRepository
import cl.hamburgpadel.data.storage.TokenManager
import cl.hamburgpadel.AdminActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var authRepository: AuthRepository
    private lateinit var tokenManager: TokenManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        // Inicializar componentes
        initializeComponents()
        
        // Verificar si ya está logueado y dirigir según el rol
        if (tokenManager.isLoggedIn()) {
            navigateBasedOnRole()
            return
        }
        
        // Configurar listener del botón de login
        loginButton.setOnClickListener {
            performLogin()
        }
    }
    
    private fun initializeComponents() {
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.loginButton)
        authRepository = AuthRepository()
        tokenManager = TokenManager(this)
    }
    
    private fun performLogin() {
        val username = editTextUsername.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        
        // Validar campos
        if (!validateInput(username, password)) {
            return
        }
        
        // Mostrar estado de carga
        setLoadingState(true)
        
        // Realizar login
        lifecycleScope.launch {
            try {
                val result = authRepository.login(username, password)
                
                result.onSuccess { loginResponse ->
                    // Guardar token
                    loginResponse.token?.let { token ->
                        tokenManager.saveAuthToken(token)
                    }
                    
                    // Guardar datos completos del usuario incluyendo rol
                    tokenManager.saveCompleteUserData(
                        id = loginResponse.id,
                        username = loginResponse.username,
                        email = loginResponse.email,
                        role = loginResponse.role
                    )
                    
                    // Mantener compatibilidad con datos del usuario anterior
                    loginResponse.user?.let { user ->
                        tokenManager.saveUserData(
                            email = user.email ?: "",
                            name = user.name ?: "",
                            uuid = user.uuid ?: ""
                        )
                    }
                    
                    showMessage("Login exitoso")
                    navigateBasedOnRole()
                    
                }.onFailure { exception ->
                    showMessage("Error: ${exception.message}")
                }
                
            } catch (e: Exception) {
                showMessage("Error de conexión: ${e.message}")
            } finally {
                setLoadingState(false)
            }
        }
    }
    
    private fun validateInput(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            editTextUsername.error = "El nombre de usuario es requerido"
            editTextUsername.requestFocus()
            return false
        }
        
        if (password.isEmpty()) {
            editTextPassword.error = "La contraseña es requerida"
            editTextPassword.requestFocus()
            return false
        }
        
        return true
    }
    
    private fun setLoadingState(isLoading: Boolean) {
        loginButton.isEnabled = !isLoading
        loginButton.text = if (isLoading) "Iniciando sesión..." else "Iniciar Sesión"
        editTextUsername.isEnabled = !isLoading
        editTextPassword.isEnabled = !isLoading
    }
    
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    private fun navigateToProgress(partidosJugados: Int = 0) {
        val intent = Intent(this, ProgressActivity::class.java)
        intent.putExtra("partidos_jugados", partidosJugados)
        startActivity(intent)
        finish()
    }
    
    private fun navigateToAdmin() {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun navigateBasedOnRole() {
        when {
            tokenManager.isAdmin() -> {
                navigateToAdmin()
            }
            tokenManager.isPlayer() -> {
                navigateToProgress()
            }
            else -> {
                 // Si no hay rol válido, limpiar sesión y mostrar error
                 tokenManager.clearSession()
                 showMessage("Error: Rol de usuario no válido")
             }
         }
     }
}