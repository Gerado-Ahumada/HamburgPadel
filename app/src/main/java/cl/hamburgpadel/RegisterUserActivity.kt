package cl.hamburgpadel

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cl.hamburgpadel.data.model.PlayerCategory
import cl.hamburgpadel.data.model.SignupRequest
import cl.hamburgpadel.data.repository.AuthRepository
import cl.hamburgpadel.data.storage.TokenManager
import kotlinx.coroutines.launch

class RegisterUserActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var spinnerPlayerCategory: Spinner
    private lateinit var buttonRegister: Button
    private lateinit var progressBar: ProgressBar
    
    private lateinit var authRepository: AuthRepository
    private lateinit var tokenManager: TokenManager
    private lateinit var categoryAdapter: ArrayAdapter<String>
    private val categories = PlayerCategory.getAllCategories()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        
        initializeComponents()
        setupCategorySpinner()
        setupRegisterButton()
    }
    
    private fun initializeComponents() {
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextPhone = findViewById(R.id.editTextPhone)
        spinnerPlayerCategory = findViewById(R.id.spinnerPlayerCategory)
        buttonRegister = findViewById(R.id.buttonRegister)
        progressBar = findViewById(R.id.progressBar)
        
        authRepository = AuthRepository()
        tokenManager = TokenManager(this)
    }
    
    private fun setupCategorySpinner() {
        val categoryNames = categories.map { it.displayName }
        categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categoryNames
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPlayerCategory.adapter = categoryAdapter
    }
    
    private fun setupRegisterButton() {
        buttonRegister.setOnClickListener {
            if (validateForm()) {
                performRegistration()
            }
        }
    }
    
    private fun validateForm(): Boolean {
        val username = editTextUsername.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        val firstName = editTextFirstName.text.toString().trim()
        val lastName = editTextLastName.text.toString().trim()
        val phone = editTextPhone.text.toString().trim()
        
        when {
            username.isEmpty() -> {
                editTextUsername.error = "El nombre de usuario es requerido"
                return false
            }
            email.isEmpty() -> {
                editTextEmail.error = "El email es requerido"
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                editTextEmail.error = "Formato de email inválido"
                return false
            }
            password.isEmpty() -> {
                editTextPassword.error = "La contraseña es requerida"
                return false
            }
            password.length < 6 -> {
                editTextPassword.error = "La contraseña debe tener al menos 6 caracteres"
                return false
            }
            firstName.isEmpty() -> {
                editTextFirstName.error = "El nombre es requerido"
                return false
            }
            lastName.isEmpty() -> {
                editTextLastName.error = "El apellido es requerido"
                return false
            }
            phone.isEmpty() -> {
                editTextPhone.error = "El teléfono es requerido"
                return false
            }
        }
        
        return true
    }
    
    private fun performRegistration() {
        setLoadingState(true)
        
        val selectedCategory = categories[spinnerPlayerCategory.selectedItemPosition]
        
        val signupRequest = SignupRequest(
            username = editTextUsername.text.toString().trim(),
            email = editTextEmail.text.toString().trim(),
            password = editTextPassword.text.toString().trim(),
            firstName = editTextFirstName.text.toString().trim(),
            lastName = editTextLastName.text.toString().trim(),
            phone = editTextPhone.text.toString().trim(),
            playerCategory = selectedCategory.value,
            role = listOf("ROLE_PLAYER")
        )
        
        lifecycleScope.launch {
            try {
                val adminToken = tokenManager.getAuthToken() ?: ""
                val result = authRepository.signup(adminToken, signupRequest)
                
                result.onSuccess { response ->
                    setLoadingState(false)
                    Toast.makeText(
                        this@RegisterUserActivity,
                        "Usuario registrado exitosamente: ${response.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    finish() // Volver a la pantalla anterior
                }.onFailure { exception ->
                    setLoadingState(false)
                    Toast.makeText(
                        this@RegisterUserActivity,
                        "Error al registrar usuario: ${exception.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                setLoadingState(false)
                Toast.makeText(
                    this@RegisterUserActivity,
                    "Error inesperado: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    
    private fun setLoadingState(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        buttonRegister.isEnabled = !isLoading
        
        // Deshabilitar todos los campos durante la carga
        editTextUsername.isEnabled = !isLoading
        editTextEmail.isEnabled = !isLoading
        editTextPassword.isEnabled = !isLoading
        editTextFirstName.isEnabled = !isLoading
        editTextLastName.isEnabled = !isLoading
        editTextPhone.isEnabled = !isLoading
        spinnerPlayerCategory.isEnabled = !isLoading
    }
}