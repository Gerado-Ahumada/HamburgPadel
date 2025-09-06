package cl.hamburgpadel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cl.hamburgpadel.ui.theme.HamburgPadelTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        val loginButton = findViewById<Button>(R.id.loginButton)

        // Establecemos un "oyente" que se activará cuando el usuario haga clic en el botón.
        loginButton.setOnClickListener {
            // --- NUEVO CÓDIGO ---
            // 1. Simulamos que obtenemos el número de partidos del backend.
            // En el futuro, este valor vendrá de tu API. Por ahora, es 7.
            val partidosJugados = 15

            // 2. Creamos la "intención" (Intent) para ir a la pantalla de Progreso.
            val intent = Intent(this, ProgressActivity::class.java)

            // 3. Adjuntamos los datos extra a la intención.
            // Le ponemos una "etiqueta" ("EXTRA_PARTIDOS_JUGADOS") para poder encontrarlo después.
            intent.putExtra("EXTRA_PARTIDOS_JUGADOS", partidosJugados)

            // 4. Ejecutamos la intención, lo que abre la nueva pantalla con los datos incluidos.
            startActivity(intent)
        }
    }
}