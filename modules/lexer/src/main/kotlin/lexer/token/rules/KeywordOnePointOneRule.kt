package lexer.token.rules

import common.enums.TypeEnum
import common.token.ConstantDeclaratorToken
import common.token.ElseToken
import common.token.IfToken
import common.token.TypeToken
import common.token.abs.TokenInterface
import lexer.token.TokenRule

class KeywordOnePointOneRule : TokenRule {
    private val keywords: List<Pair<String, (Int, Int) -> common.token.abs.TokenInterface>> =
        listOf(
            "Boolean" to { r, c -> common.token.TypeToken(common.enums.TypeEnum.BOOLEAN, r, c) },
            "const" to { r, c -> common.token.ConstantDeclaratorToken(r, c) },
            "if" to { r, c -> common.token.IfToken(r, c) },
            "else" to { r, c -> common.token.ElseToken(r, c) },
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
