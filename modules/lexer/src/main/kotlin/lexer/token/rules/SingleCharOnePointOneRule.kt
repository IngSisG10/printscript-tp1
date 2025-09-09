package lexer.token.rules

import lexer.token.TokenRule

class SingleCharOnePointOneRule : TokenRule {
    private val map: Map<Char, (Int, Int) -> common.token.abs.TokenInterface> =
        mapOf(
            '{' to { r, c -> common.token.OpenBraceToken(r, c) },
            '}' to { r, c -> common.token.CloseBraceToken(r, c) },
        )

    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): TokenRule.MatchResult? {
        val ch = line[index]
        val factory = map[ch] ?: return null
        return TokenRule.MatchResult(factory(row, index), index + 1)
    }
}
