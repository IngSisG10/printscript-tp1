package lexer.token.rules

import common.enums.FunctionEnum
import common.enums.TypeEnum
import common.token.BooleanLiteralToken
import common.token.ConstantDeclaratorToken
import common.token.ElseToken
import common.token.FunctionToken
import common.token.IfToken
import common.token.TypeToken
import common.token.abs.TokenInterface
import lexer.token.TokenRule
import lexer.token.rules.util.RegexUtil.Companion.buildRegex

class KeywordOnePointOneRule : TokenRule {
    private val keywords: List<Triple<String, Regex, (Int, Int) -> TokenInterface>> =
        listOf(
            "boolean" to { r: Int, c: Int -> TypeToken(TypeEnum.BOOLEAN, r, c) },
            "const" to { r, c -> ConstantDeclaratorToken(r, c) },
            "if" to { r, c -> IfToken(r, c) },
            "else" to { r, c -> ElseToken(r, c) },
            "readInput" to { r, c -> FunctionToken(FunctionEnum.READ_INPUT, r, c) },
            "readEnv" to { r, c -> FunctionToken(FunctionEnum.READ_ENV, r, c) },
            "true" to { r, c -> BooleanLiteralToken(true, r, c) },
            "false" to { r, c -> BooleanLiteralToken(false, r, c) },
        ).map { (w, f) -> Triple(w, buildRegex(w), f) }

    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): TokenRule.MatchResult? {
        for ((word, regex, factory) in keywords) {
            val m = regex.matchAt(line, index) // match exacto desde index
            if (m != null) {
                return TokenRule.MatchResult(factory(row, index), index + word.length)
            }
        }
        return null
    }
}
