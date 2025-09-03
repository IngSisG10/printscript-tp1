package lexer.token.rules

import common.enums.FunctionEnum
import common.enums.TypeEnum
import common.token.FunctionToken
import common.token.TypeToken
import common.token.VariableDeclaratorToken
import common.token.abs.TokenInterface
import lexer.token.TokenRule

class KeywordRule : TokenRule {
    private val keywords: List<Pair<String, (Int, Int) -> common.token.abs.TokenInterface>> =
        listOf(
            "println" to { r, c -> common.token.FunctionToken(common.enums.FunctionEnum.PRINTLN, r, c) },
            "let" to { r, c -> common.token.VariableDeclaratorToken(r, c) },
            "String" to { r, c -> common.token.TypeToken(common.enums.TypeEnum.STRING, r, c) },
            "Number" to { r, c -> common.token.TypeToken(common.enums.TypeEnum.NUMBER, r, c) },
            "Any" to { r, c -> common.token.TypeToken(common.enums.TypeEnum.ANY, r, c) },
        )

    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): TokenRule.MatchResult? {
        for ((lexeme, factory) in keywords) {
            if (line.startsWith(lexeme, index)) {
                return TokenRule.MatchResult(factory(row, index), index + lexeme.length)
            }
        }
        return null
    }
}
