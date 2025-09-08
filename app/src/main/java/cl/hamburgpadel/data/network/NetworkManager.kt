package cl.hamburgpadel.data.network

import cl.hamburgpadel.data.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton que maneja la configuración de red y Retrofit
 */
object NetworkManager {
    
    // Configuración para backend local
    private const val BASE_URL = "http://10.0.2.2:3000/api/" // Para emulador Android
    // private const val BASE_URL = "http://localhost:3000/api/" // Para pruebas en navegador
    // private const val BASE_URL = "http://192.168.1.XXX:3000/api/" // Para dispositivo físico (cambiar XXX por tu IP)
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}