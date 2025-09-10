package cl.hamburgpadel.data.model

enum class PlayerCategory(val displayName: String, val value: String) {
    // Categorías Varones
    PRIMERA_VARON("1ra Categoría Varones", "1ra"),
    SEGUNDA_VARON("2da Categoría Varones", "2da"),
    TERCERA_VARON("3ra Categoría Varones", "3ra"),
    CUARTA_VARON("4ta Categoría Varones", "4ta"),
    QUINTA_VARON("5ta Categoría Varones", "5ta"),
    SEXTA_VARON("6ta Categoría Varones", "6ta"),
    
    // Categorías Damas
    CATEGORIA_A_DAMAS("Categoría A Damas", "A"),
    CATEGORIA_B_DAMAS("Categoría B Damas", "B"),
    CATEGORIA_C_DAMAS("Categoría C Damas", "C"),
    CATEGORIA_D_DAMAS("Categoría D Damas", "D"),
    
    // Categoría Amateur
    AMATEUR("Amateur", "Amateur");
    
    companion object {
        fun getVaronesCategories(): List<PlayerCategory> {
            return listOf(PRIMERA_VARON, SEGUNDA_VARON, TERCERA_VARON, CUARTA_VARON, QUINTA_VARON, SEXTA_VARON)
        }
        
        fun getDamasCategories(): List<PlayerCategory> {
            return listOf(CATEGORIA_A_DAMAS, CATEGORIA_B_DAMAS, CATEGORIA_C_DAMAS, CATEGORIA_D_DAMAS)
        }
        
        fun getAllCategories(): List<PlayerCategory> {
            return values().toList()
        }
    }
}