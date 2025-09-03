package lexer.token.rules

import common.enums.OperationEnum
import common.token.EndSentenceToken
import common.token.OperationToken
import common.token.PointToken
import common.token.TypeDeclaratorToken
import common.token.abs.TokenInterface
import lexer.token.TokenRule

class SingleCharRule : TokenRule {
    private val map: Map<Char, (Int, Int) -> common.token.abs.TokenInterface> =
        mapOf(
            '+' to { r, c -> common.token.OperationToken(common.enums.OperationEnum.SUM, r, c) },
            '-' to { r, c -> common.token.OperationToken(common.enums.OperationEnum.MINUS, r, c) },
            '*' to { r, c -> common.token.OperationToken(common.enums.OperationEnum.MULTIPLY, r, c) },
            '/' to { r, c -> common.token.OperationToken(common.enums.OperationEnum.DIVIDE, r, c) },
            '=' to { r, c -> common.token.OperationToken(common.enums.OperationEnum.EQUAL, r, c) },
            ':' to { r, c -> common.token.TypeDeclaratorToken(r, c) },
            '.' to { r, c -> common.token.PointToken(r, c) },
            ';' to { r, c -> common.token.EndSentenceToken(r, c) },
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
