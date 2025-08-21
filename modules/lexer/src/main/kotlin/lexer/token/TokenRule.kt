package lexer.token

import token.abs.TokenInterface

interface TokenRule {
    fun match(
        line: String,
        index: Int,
        row: Int,
    ): MatchResult?

    data class MatchResult(
        val token: TokenInterface?,
        val nextIndex: Int,
        val rowDelta: Int = 0,
    )
}
