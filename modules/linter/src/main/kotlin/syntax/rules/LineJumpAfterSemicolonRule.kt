package syntax.rules

import data.LinterData
import exception.NoNewLineAfterSemiColon
import syntax.LinterRule
import token.EndSentenceToken
import token.NewLineToken
import token.abs.TokenInterface

class LineJumpAfterSemicolonRule : LinterRule {
    override fun match(tokens: List<TokenInterface>): Exception? {
        for ((index, token) in tokens.withIndex()) {
            if (token is EndSentenceToken) {
                val next = tokens.getOrNull(index + 1)

                if (next !is NewLineToken) {
                    return NoNewLineAfterSemiColon()
                }
            }
        }
        return null
    }

    override fun matchWithData(tokens: List<TokenInterface>): List<LinterData> {
        val list = mutableListOf<LinterData>()
        for ((index, token) in tokens.withIndex()) {
            if (token is EndSentenceToken) {
                val next = tokens.getOrNull(index + 1)
                if (next !is NewLineToken) {
                    list.add(
                        LinterData(
                            exception = NoNewLineAfterSemiColon(),
                            position = index,
                        ),
                    )
                }
            }
        }
        return list
    }
}
