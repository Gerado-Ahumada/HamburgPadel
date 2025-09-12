package cl.hamburgpadel

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cl.hamburgpadel.adapters.UsersAdapter
import cl.hamburgpadel.data.models.Player
import cl.hamburgpadel.data.models.PlayersResponse
import cl.hamburgpadel.data.repository.AuthRepository
import cl.hamburgpadel.data.storage.TokenManager
import kotlinx.coroutines.launch

class ListUsersActivity : AppCompatActivity() {

    private lateinit var buttonBack: Button
    private lateinit var editTextFilterName: EditText
    private lateinit var spinnerFilterCategory: Spinner
    private lateinit var buttonSearch: Button
    private lateinit var recyclerViewUsers: RecyclerView
    private lateinit var buttonPrevious: Button
    private lateinit var buttonNext: Button
    private lateinit var textViewPagination: TextView
    private lateinit var progressBar: ProgressBar
    
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var authRepository: AuthRepository
    private lateinit var tokenManager: TokenManager
    
    private var currentPage = 0
    private var totalPages = 0
    private var currentResponse: PlayersResponse? = null
    
    private val categories = listOf(
        "Todas las categorías",
        "SENIOR",
        "JUNIOR", 
        "AMATEUR",
        "PROFESIONAL"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_users)
        
        initializeComponents()
        setupRecyclerView()
        setupCategorySpinner()
        setupClickListeners()
        
        // Cargar usuarios inicialmente
        loadUsers()
    }
    
    private fun initializeComponents() {
        buttonBack = findViewById(R.id.buttonBack)
        editTextFilterName = findViewById(R.id.editTextFilterName)
        spinnerFilterCategory = findViewById(R.id.spinnerFilterCategory)
        buttonSearch = findViewById(R.id.buttonSearch)
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers)
        buttonPrevious = findViewById(R.id.buttonPrevious)
        buttonNext = findViewById(R.id.buttonNext)
        textViewPagination = findViewById(R.id.textViewPagination)
        progressBar = findViewById(R.id.progressBar)
        
        authRepository = AuthRepository()
        tokenManager = TokenManager(this)
    }
    
    private fun setupRecyclerView() {
        usersAdapter = UsersAdapter { player ->
            // Navegar a EditUserActivity
            val intent = Intent(this, EditUserActivity::class.java)
            intent.putExtra("PLAYER_UUID", player.uuid)
            intent.putExtra("PLAYER_USERNAME", player.username)
            intent.putExtra("PLAYER_FULL_NAME", player.fullName)
            intent.putExtra("PLAYER_EMAIL", player.email)
            intent.putExtra("PLAYER_PHONE", player.phone)
            intent.putExtra("PLAYER_CATEGORY", player.playerCategory)
            intent.putExtra("PLAYER_STATUS", player.status)
            startActivity(intent)
        }
        
        recyclerViewUsers.apply {
            layoutManager = LinearLayoutManager(this@ListUsersActivity)
            adapter = usersAdapter
        }
    }
    
    private fun setupCategorySpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFilterCategory.adapter = adapter
    }
    
    private fun setupClickListeners() {
        buttonBack.setOnClickListener {
            finish() // Regresa a AdminActivity
        }
        
        buttonSearch.setOnClickListener {
            currentPage = 0
            loadUsers()
        }
        
        buttonPrevious.setOnClickListener {
            if (currentPage > 0) {
                currentPage--
                loadUsers()
            }
        }
        
        buttonNext.setOnClickListener {
            if (currentPage < totalPages - 1) {
                currentPage++
                loadUsers()
            }
        }
    }
    
    private fun loadUsers() {
        val token = tokenManager.getAuthToken()
        if (token == null) {
            showMessage("Token de sesión no encontrado")
            finish()
            return
        }
        
        setLoadingState(true)
        
        val nameFilter = editTextFilterName.text.toString().trim().takeIf { it.isNotEmpty() }
        val categoryFilter = if (spinnerFilterCategory.selectedItemPosition == 0) {
            null // "Todas las categorías"
        } else {
            categories[spinnerFilterCategory.selectedItemPosition]
        }
        
        lifecycleScope.launch {
            try {
                val result = authRepository.getUsersList(
                    token = token,
                    name = nameFilter,
                    category = categoryFilter,
                    page = currentPage,
                    size = 10
                )
                
                result.onSuccess { response ->
                    currentResponse = response
                    totalPages = response.totalPages
                    
                    usersAdapter.updateUsers(response.players)
                    updatePaginationControls()
                    
                    if (response.players.isEmpty()) {
                        showMessage("No se encontraron usuarios")
                    }
                    
                }.onFailure { exception ->
                    showMessage("Error al cargar usuarios: ${exception.message}")
                    usersAdapter.clearUsers()
                }
                
            } catch (e: Exception) {
                showMessage("Error inesperado: ${e.message}")
                usersAdapter.clearUsers()
            } finally {
                setLoadingState(false)
            }
        }
    }
    
    private fun updatePaginationControls() {
        val response = currentResponse ?: return
        
        // Actualizar información de página
        textViewPagination.text = "Página ${currentPage + 1} de ${totalPages}"
        
        // Habilitar/deshabilitar botones
        buttonPrevious.isEnabled = response.hasPrevious
        buttonNext.isEnabled = response.hasNext
        
        // Cambiar apariencia visual de los botones
        buttonPrevious.alpha = if (response.hasPrevious) 1.0f else 0.5f
        buttonNext.alpha = if (response.hasNext) 1.0f else 0.5f
    }
    
    private fun setLoadingState(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        buttonSearch.isEnabled = !isLoading
        buttonPrevious.isEnabled = !isLoading && (currentResponse?.hasPrevious == true)
        buttonNext.isEnabled = !isLoading && (currentResponse?.hasNext == true)
    }
    
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}