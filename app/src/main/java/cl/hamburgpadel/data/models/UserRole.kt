package cl.hamburgpadel.data.models

/**
 * Enum que define los roles de usuario en la aplicación
 */
enum class UserRole(val roleName: String) {
    PLAYER("ROLE_PLAYER"),
    ADMIN("ROLE_ADMIN");
    
    companion object {
        /**
         * Convierte un string del backend al enum correspondiente
         */
        fun fromString(role: String?): UserRole? {
            return when (role) {
                "ROLE_PLAYER" -> PLAYER
                "ROLE_ADMIN" -> ADMIN
                else -> null
            }
        }
    }
    
    /**
     * Verifica si el usuario es un jugador
     */
    fun isPlayer(): Boolean = this == PLAYER
    
    /**
     * Verifica si el usuario es un administrador
     */
    fun isAdmin(): Boolean = this == ADMIN
}