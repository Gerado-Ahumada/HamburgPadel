package cl.hamburgpadel.data.model

data class SignupResponse(
    val message: String,
    val success: Boolean,
    val userId: Long? = null
)