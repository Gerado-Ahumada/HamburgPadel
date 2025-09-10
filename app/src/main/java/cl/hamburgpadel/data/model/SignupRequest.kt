package cl.hamburgpadel.data.model

data class SignupRequest(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val playerCategory: String,
    val role: List<String> = listOf("ROLE_PLAYER")
)