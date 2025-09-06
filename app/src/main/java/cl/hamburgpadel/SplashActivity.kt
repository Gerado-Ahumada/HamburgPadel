package cl.hamburgpadel



import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import cl.hamburgpadel.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    // Define la duración de la splash screen en milisegundos
    private val SPLASH_DURATION: Long = 5000 // 3 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Usamos un Handler para retrasar la transición a la siguiente pantalla
        Handler(Looper.getMainLooper()).postDelayed({
            // Este código se ejecutará después de que pase el tiempo de SPLASH_DURATION

            // Crea la intención para ir a MainActivity (la pantalla de login)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Finaliza esta actividad para que el usuario no pueda volver a ella
            finish()

        }, SPLASH_DURATION)
    }
}