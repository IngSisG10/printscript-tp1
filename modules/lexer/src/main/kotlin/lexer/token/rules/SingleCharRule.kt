package lexer.token.rules

import enums.OperationEnum
import lexer.token.TokenRule
import token.EndSentenceToken
import token.OperationToken
import token.PointToken
import token.TypeDeclaratorToken
import token.abs.TokenInterface

class SingleCharRule : TokenRule {
    private val map: Map<Char, (Int, Int) -> TokenInterface> =
        mapOf(
            '+' to { r, c -> OperationToken(OperationEnum.SUM, r, c) },
            '-' to { r, c -> OperationToken(OperationEnum.MINUS, r, c) },
            '*' to { r, c -> OperationToken(OperationEnum.MULTIPLY, r, c) },
            '/' to { r, c -> OperationToken(OperationEnum.DIVIDE, r, c) },
            '=' to { r, c -> OperationToken(OperationEnum.EQUAL, r, c) },
            ':' to { r, c -> TypeDeclaratorToken(r, c) },
            '.' to { r, c -> PointToken(r, c) },
            ';' to { r, c -> EndSentenceToken(r, c) },
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
