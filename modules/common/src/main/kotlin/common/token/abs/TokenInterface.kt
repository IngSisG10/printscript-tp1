package common.token.abs

data class TokenPosition(
    val row: Int,
    val pos: Int,
)

interface TokenInterface {
    val name: String // STRING_TYPE
    val value: Any // "Olive"
    val row: Int
    val position: Int

    fun getPosition(): common.token.abs.TokenPosition = common.token.abs.TokenPosition(row, position)
}
