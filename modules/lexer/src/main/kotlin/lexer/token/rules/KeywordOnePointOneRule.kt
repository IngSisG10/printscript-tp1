package lexer.token.rules

import enums.TypeEnum
import lexer.token.TokenRule
import token.ConstantDeclaratorToken
import token.ElseToken
import token.IfToken
import token.TypeToken
import token.abs.TokenInterface

class KeywordOnePointOneRule : TokenRule {
    private val keywords: List<Pair<String, (Int, Int) -> TokenInterface>> =
        listOf(
            "Boolean" to { r, c -> TypeToken(TypeEnum.BOOLEAN, r, c) },
            "const" to { r, c -> ConstantDeclaratorToken(r, c) },
            "if" to { r, c -> IfToken(r, c) },
            "else" to { r, c -> ElseToken(r, c) },
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
