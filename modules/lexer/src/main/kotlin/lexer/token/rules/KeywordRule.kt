package lexer.token.rules

import common.enums.FunctionEnum
import common.enums.TypeEnum
import common.token.FunctionToken
import common.token.TypeToken
import common.token.VariableDeclaratorToken
import common.token.abs.TokenInterface
import lexer.token.TokenRule
import lexer.token.rules.util.RegexUtil.Companion.buildRegex

class KeywordRule : TokenRule {
    private val keywords: List<Triple<String, Regex, (Int, Int) -> TokenInterface>> =
        listOf(
            "println" to { r: Int, c: Int -> FunctionToken(FunctionEnum.PRINTLN, r, c) },
            "let" to { r, c -> VariableDeclaratorToken(r, c) },
            "string" to { r, c -> TypeToken(TypeEnum.STRING, r, c) },
            "number" to { r, c -> TypeToken(TypeEnum.NUMBER, r, c) },
            "any" to { r, c -> TypeToken(TypeEnum.ANY, r, c) },
        ).map { (w, f) -> Triple(w, buildRegex(w), f) }

    override fun match(
        line: String,
        index: Int,
        row: Int,
    ): TokenRule.MatchResult? {
        for ((word, regex, factory) in keywords) {
            val m = regex.matchAt(line, index)
            if (m != null) {
                return TokenRule.MatchResult(factory(row, index), index + word.length)
            }
        }
        return null
    }
}
