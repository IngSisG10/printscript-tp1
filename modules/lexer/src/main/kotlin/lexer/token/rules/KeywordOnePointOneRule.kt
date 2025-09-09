package lexer.token.rules

import common.enums.FunctionEnum
import common.enums.TypeEnum
import common.token.ConstantDeclaratorToken
import common.token.ElseToken
import common.token.FunctionToken
import common.token.IfToken
import common.token.TypeToken
import common.token.abs.TokenInterface
import lexer.token.TokenRule

class KeywordOnePointOneRule : TokenRule {
    private val keywords: List<Pair<String, (Int, Int) -> TokenInterface>> =
        listOf(
            "Boolean" to { r, c -> TypeToken(TypeEnum.BOOLEAN, r, c) },
            "const" to { r, c -> ConstantDeclaratorToken(r, c) },
            "if" to { r, c -> IfToken(r, c) },
            "else" to { r, c -> ElseToken(r, c) },
            "readInput" to { r, c -> FunctionToken(FunctionEnum.READ_INPUT, r, c) },
            "readEnv" to { r, c -> FunctionToken(FunctionEnum.READ_ENV, r, c) },
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
