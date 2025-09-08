package cl.hamburgpadel

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cl.hamburgpadel.data.repository.AuthRepository
import cl.hamburgpadel.data.storage.TokenManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var authRepository: AuthRepository
    private lateinit var tokenManager: TokenManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        // Inicializar componentes
        initializeComponents()
        
        // Verificar si ya está logueado
        if (tokenManager.isLoggedIn()) {
            navigateToProgress()
            return
        }
        
        // Configurar listener del botón de login
        loginButton.setOnClickListener {
            performLogin()
        }
    }
    
    private fun initializeComponents() {
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.loginButton)
        authRepository = AuthRepository()
        tokenManager = TokenManager(this)
    }
    
    private fun performLogin() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        
        // Validar campos
        if (!validateInput(email, password)) {
            return
        }
        
        // Mostrar estado de carga
        setLoadingState(true)
        
        // Realizar login
        lifecycleScope.launch {
            try {
                val result = authRepository.login(email, password)
                
                result.onSuccess { loginResponse ->
                    // Guardar token y datos del usuario
                    loginResponse.token?.let { token ->
                        tokenManager.saveAuthToken(token)
                    }
                    
                    loginResponse.user?.let { user ->
                        tokenManager.saveUserData(
                            email = user.email ?: email,
                            name = user.name ?: "",
                            uuid = user.uuid ?: ""
                        )
                    }
                    
                    showMessage("Login exitoso")
                    navigateToProgress(loginResponse.user?.partidosJugados ?: 0)
                    
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
    
    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            editTextEmail.error = "El email es requerido"
            editTextEmail.requestFocus()
            return false
        }
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Ingresa un email válido"
            editTextEmail.requestFocus()
            return false
        }
        
        if (password.isEmpty()) {
            editTextPassword.error = "La contraseña es requerida"
            editTextPassword.requestFocus()
            return false
        }
        
        if (password.length < 6) {
            editTextPassword.error = "La contraseña debe tener al menos 6 caracteres"
            editTextPassword.requestFocus()
            return false
        }
        
        return true
    }
    
    private fun setLoadingState(isLoading: Boolean) {
        loginButton.isEnabled = !isLoading
        loginButton.text = if (isLoading) "Iniciando sesión..." else "Iniciar Sesión"
        editTextEmail.isEnabled = !isLoading
        editTextPassword.isEnabled = !isLoading
    }
    
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    private fun navigateToProgress(partidosJugados: Int = 0) {
        val intent = Intent(this, ProgressActivity::class.java)
        intent.putExtra("EXTRA_PARTIDOS_JUGADOS", partidosJugados)
        startActivity(intent)
        finish() // Cerrar MainActivity para que no se pueda volver con el botón atrás
    }
}