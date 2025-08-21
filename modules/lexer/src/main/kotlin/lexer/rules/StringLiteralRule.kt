package lexer.rules

import token.StringLiteralToken

class StringLiteralRule : TokenRule {
    private val regex = Regex("\"(.*?)\"")

    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): TokenRule.MatchResult? {
        val m = regex.find(line, index)?.takeIf { it.range.first == index } ?: return null
        val token = StringLiteralToken(m.groupValues[1], row, index)
        return TokenRule.MatchResult(token, m.range.last + 1)
    }
}
