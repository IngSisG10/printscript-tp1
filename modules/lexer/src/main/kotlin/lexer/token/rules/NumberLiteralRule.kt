package lexer.token.rules

import common.token.NumberLiteralToken
import lexer.token.TokenRule

class NumberLiteralRule : TokenRule {
    private val regex = Regex("\\d+")

    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): TokenRule.MatchResult? {
        val m = regex.find(line, index)?.takeIf { it.range.first == index } ?: return null
        val token = common.token.NumberLiteralToken(m.value.toInt(), row, index)
        return TokenRule.MatchResult(token, m.range.last + 1)
    }
}
