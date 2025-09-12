package cl.hamburgpadel

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditUserActivity : AppCompatActivity() {

    private lateinit var textViewUserInfo: TextView
    private lateinit var buttonSave: Button
    private lateinit var buttonCancel: Button
    
    private var playerUuid: String? = null
    private var playerUsername: String? = null
    private var playerFullName: String? = null
    private var playerEmail: String? = null
    private var playerPhone: String? = null
    private var playerCategory: String? = null
    private var playerStatus: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)
        
        initializeComponents()
        loadUserData()
        setupClickListeners()
    }
    
    private fun initializeComponents() {
        textViewUserInfo = findViewById(R.id.textViewUserInfo)
        buttonSave = findViewById(R.id.buttonSave)
        buttonCancel = findViewById(R.id.buttonCancel)
    }
    
    private fun loadUserData() {
        // Obtener datos del intent
        playerUuid = intent.getStringExtra("PLAYER_UUID")
        playerUsername = intent.getStringExtra("PLAYER_USERNAME")
        playerFullName = intent.getStringExtra("PLAYER_FULL_NAME")
        playerEmail = intent.getStringExtra("PLAYER_EMAIL")
        playerPhone = intent.getStringExtra("PLAYER_PHONE")
        playerCategory = intent.getStringExtra("PLAYER_CATEGORY")
        playerStatus = intent.getStringExtra("PLAYER_STATUS")
        
        // Mostrar información del usuario
        val userInfo = buildString {
            appendLine("UUID: $playerUuid")
            appendLine("Usuario: $playerUsername")
            appendLine("Nombre: $playerFullName")
            appendLine("Email: $playerEmail")
            appendLine("Teléfono: $playerPhone")
            appendLine("Categoría: $playerCategory")
            appendLine("Estado: $playerStatus")
        }
        
        textViewUserInfo.text = userInfo
    }
    
    private fun setupClickListeners() {
        buttonSave.setOnClickListener {
            // TODO: Implementar lógica de guardado
            Toast.makeText(this, "Funcionalidad de guardado pendiente de implementar", Toast.LENGTH_SHORT).show()
        }
        
        buttonCancel.setOnClickListener {
            finish()
        }
    }
}