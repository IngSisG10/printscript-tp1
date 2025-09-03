package lexer.token

import common.token.abs.TokenInterface

interface TokenRule {
    fun match(
        line: String,
        index: Int,
        row: Int,
    ): MatchResult?

    data class MatchResult(
        val token: common.token.abs.TokenInterface?,
        val nextIndex: Int,
        val rowDelta: Int = 0,
    )
}
