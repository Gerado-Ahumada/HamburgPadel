package cl.hamburgpadel

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.UUID

// El Data class no cambia, sigue siendo perfecto para representar una recompensa.
data class Reward(
    val requiredMatches: Int,
    val description: String,
    val iconResId: Int
)

class ProgressActivity : AppCompatActivity() {

    // --- PASO 1: SIMULACIÓN DE DATOS DEL USUARIO ---
    // Estos datos vendrán del backend después del login. Por ahora, los simulamos aquí.
    private val fakeUserName = "Socio PadelPro"
    private val fakeUserUuid = UUID.randomUUID().toString()
    private val fakeSessionToken = "ABC-123-DEF-456-GHI-789"
    // ---------------------------------------------

    private lateinit var ballImageViews: List<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        val partidosJugados = intent.getIntExtra("EXTRA_PARTIDOS_JUGADOS", 0)

        initializeViews()
        updateProgressUI(partidosJugados)
    }

    private fun initializeViews() {
        ballImageViews = listOf(
            findViewById(R.id.ball_1), findViewById(R.id.ball_2), findViewById(R.id.ball_3), findViewById(R.id.ball_4),
            findViewById(R.id.ball_5), findViewById(R.id.ball_6), findViewById(R.id.ball_7), findViewById(R.id.ball_8),
            findViewById(R.id.ball_9), findViewById(R.id.ball_10), findViewById(R.id.ball_11), findViewById(R.id.ball_12),
            findViewById(R.id.ball_13), findViewById(R.id.ball_14), findViewById(R.id.ball_15), findViewById(R.id.ball_16)
        )
    }

    private fun updateProgressUI(partidosJugados: Int) {
        val tvMatchesPlayed: TextView = findViewById(R.id.tv_matches_played)
        tvMatchesPlayed.text = "$partidosJugados de 16 partidos"

        updateBallsGrid(partidosJugados)
        updateRewardsList(partidosJugados) // Esta es la función clave que hemos modificado.
    }

    private fun updateBallsGrid(partidosJugados: Int) {
        val benefitMatches = listOf(4, 8, 12, 16)

        ballImageViews.forEachIndexed { index, imageView ->
            val matchNumber = index + 1
            val isBenefit = benefitMatches.contains(matchNumber)

            if (matchNumber <= partidosJugados) {
                imageView.alpha = 1.0f
                if (isBenefit) {
                    imageView.setBackgroundResource(R.drawable.ball_benefit)
                } else {
                    imageView.background = null
                }
            } else {
                imageView.alpha = 0.3f
                imageView.background = null
            }
        }
    }

    private fun updateRewardsList(partidosJugados: Int) {
        val rewardsContainer: LinearLayout = findViewById(R.id.container_rewards)
        rewardsContainer.removeAllViews()

        val rewards = listOf(
            Reward(4, "Bebida isotónica gratis", R.drawable.isotonica),
            Reward(8, "Bote de pelotas nuevo", R.drawable.pelota),
            Reward(12, "Descuento en tienda", R.drawable.tienda),
            Reward(16, "Partido gratis", R.drawable.pala)
        )

        rewards.forEach { reward ->
            val isUnlocked = partidosJugados >= reward.requiredMatches
            val rewardView = LayoutInflater.from(this).inflate(R.layout.item_reward, rewardsContainer, false)

            val icon: ImageView = rewardView.findViewById(R.id.iv_reward_icon)
            val text: TextView = rewardView.findViewById(R.id.tv_reward_text)
            val status: ImageView = rewardView.findViewById(R.id.iv_reward_status)

            icon.setImageResource(reward.iconResId)
            text.text = reward.description

            if (isUnlocked) {
                // El beneficio está desbloqueado
                status.setImageResource(R.drawable.ic_check_circle)
                rewardView.alpha = 1.0f

                // --- PASO 2: HACER LA RECOMPENSA CLICABLE ---
                // Si la recompensa está desbloqueada, le añadimos un listener
                // para que al hacer clic, se abra la pantalla del QR.
                rewardView.setOnClickListener {
                    openQrScreen()
                }
                // -----------------------------------------------

            } else {
                // El beneficio está bloqueado
                status.setImageResource(R.drawable.ic_block)
                rewardView.alpha = 0.5f
                rewardView.isClickable = false // Opcional: deshabilitar explícitamente el clic
            }

            rewardsContainer.addView(rewardView)
        }
    }

    // --- PASO 3: NUEVA FUNCIÓN PARA ABRIR LA PANTALLA DEL QR ---
    private fun openQrScreen() {
        // Creamos una "intención" para navegar a QrCodeActivity
        val intent = Intent(this, QrCodeActivity::class.java)

        // Adjuntamos los datos del usuario (simulados por ahora)
        intent.putExtra("USER_NAME", fakeUserName)
        intent.putExtra("USER_UUID", fakeUserUuid)
        intent.putExtra("SESSION_TOKEN", fakeSessionToken)

        // Iniciamos la nueva actividad
        startActivity(intent)
    }
    // -------------------------------------------------------------
}