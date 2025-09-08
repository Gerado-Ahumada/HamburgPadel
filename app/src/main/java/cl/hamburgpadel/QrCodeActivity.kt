package cl.hamburgpadel

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.google.zxing.BarcodeFormat
import cl.hamburgpadel.databinding.ActivityQrCodeBinding

class QrCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Conecta este archivo Kotlin con su diseño XML
        setContentView(R.layout.activity_qr_code)
        binding = ActivityQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Recibe los datos que la pantalla anterior (ProgressActivity) le envió.
        // Usamos el operador Elvis (?:) para tener un valor por defecto en caso de que un dato no llegue.
        val userName = intent.getStringExtra("USER_NAME") ?: "N/A"
        val userUuid = intent.getStringExtra("USER_UUID") ?: "N/A"
        val sessionToken = intent.getStringExtra("SESSION_TOKEN") ?: "N/A"
        val activityDateTime = intent.getStringExtra("ACTIVITY_DATETIME") ?: "N/A"

        // Busca el ImageView en el layout por su ID.
        val qrImageView = findViewById<ImageView>(R.id.iv_qr_code)

        // 2. Combina toda la información en un solo texto.
        // Usar un formato como JSON es una buena práctica para que sea fácil de leer por un escáner.
        val qrContent = """
        {
            "userName": "$userName",
            "uuid": "$userUuid",
            "token": "$sessionToken",
            "activityDateTime": "$activityDateTime"
        }
        """.trimIndent()

        try {
            // 3. Usa la librería ZXing para generar el código QR como una imagen (Bitmap).
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(qrContent, BarcodeFormat.QR_CODE, 400, 400)

            // 4. Muestra la imagen del QR recién creada en el ImageView.
            qrImageView.setImageBitmap(bitmap)

        } catch (e: Exception) {
            // Si algo sale mal al generar el QR, se registrará el error en la consola de depuración.
            e.printStackTrace()
        }
        binding.backButton.setOnClickListener {
            finish()
        }    }
}