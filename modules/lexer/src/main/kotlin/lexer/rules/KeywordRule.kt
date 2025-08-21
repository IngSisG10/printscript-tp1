package lexer.rules

import enums.FunctionEnum
import enums.TypeEnum
import token.FunctionToken
import token.TypeToken
import token.VariableDeclaratorToken
import token.abs.TokenInterface

class KeywordRule : TokenRule {
    private val keywords: List<Pair<String, (Int, Int) -> TokenInterface>> =
        listOf(
            "println" to { r, c -> FunctionToken(FunctionEnum.PRINTLN, r, c) },
            "let" to { r, c -> VariableDeclaratorToken(r, c) },
            "String" to { r, c -> TypeToken(TypeEnum.STRING, r, c) },
            "Number" to { r, c -> TypeToken(TypeEnum.NUMBER, r, c) },
            "Boolean" to { r, c -> TypeToken(TypeEnum.BOOLEAN, r, c) },
            "Any" to { r, c -> TypeToken(TypeEnum.ANY, r, c) },
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
