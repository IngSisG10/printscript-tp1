package org.example.token.abs

data class TokenPosition(val row: Int, val pos: Int)

interface TokenInterface {
    val name: String // STRING_TYPE
    val value: Any // "Olive"
    val row: Int
    val position: Int
    fun getPosition(): TokenPosition {
        return TokenPosition(row , position)
    }
}