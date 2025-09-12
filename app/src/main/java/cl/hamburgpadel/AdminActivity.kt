package cl.hamburgpadel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import cl.hamburgpadel.data.storage.TokenManager

/**
 * Activity para administradores del sistema
 * Pantalla en blanco preparada para futuras funcionalidades
 */
class AdminActivity : AppCompatActivity() {
    
    private lateinit var tokenManager: TokenManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        
        // Inicializar TokenManager
        tokenManager = TokenManager(this)
        
        // Configurar UI
        setupUI()
        
        // Configurar listeners del menú
        setupMenuListeners()
        
        // Configurar botón de listar usuarios
        setupListUsersButton()
        
        // Configurar botón de logout
        setupLogoutButton()
    }
    
    private fun setupUI() {
        val welcomeText = findViewById<TextView>(R.id.textViewAdminWelcome)
        val username = tokenManager.getUsername() ?: "Administrador"
        welcomeText.text = "Bienvenido, $username"
    }
    
    private fun setupMenuListeners() {
        // Registrar nuevo usuario
        findViewById<CardView>(R.id.cardViewRegisterUser).setOnClickListener {
            val intent = Intent(this, RegisterUserActivity::class.java)
            startActivity(intent)
        }
        
        // Reporte del Mes
        findViewById<CardView>(R.id.cardViewMonthReport).setOnClickListener {
            Toast.makeText(this, "Reporte del Mes - Próximamente", Toast.LENGTH_SHORT).show()
        }
        
        // Reporte del Día
        findViewById<CardView>(R.id.cardViewDayReport).setOnClickListener {
            Toast.makeText(this, "Reporte del Día - Próximamente", Toast.LENGTH_SHORT).show()
        }
        
        // Registrar actividad
        findViewById<CardView>(R.id.cardViewRegisterActivity).setOnClickListener {
            Toast.makeText(this, "Registrar actividad - Próximamente", Toast.LENGTH_SHORT).show()
        }
        
        // Cobrar Beneficio
        findViewById<CardView>(R.id.cardViewCollectBenefit).setOnClickListener {
            Toast.makeText(this, "Cobrar Beneficio - Próximamente", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupListUsersButton() {
        val cardViewListUsers = findViewById<androidx.cardview.widget.CardView>(R.id.cardViewListUsers)
        cardViewListUsers.setOnClickListener {
            val intent = Intent(this, ListUsersActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun setupLogoutButton() {
        val logoutButton = findViewById<Button>(R.id.buttonAdminLogout)
        logoutButton.setOnClickListener {
            logout()
        }
    }
    
    private fun logout() {
        // Limpiar todos los datos almacenados
        tokenManager.clearSession()
        
        // Navegar de vuelta al MainActivity (pantalla de login)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}