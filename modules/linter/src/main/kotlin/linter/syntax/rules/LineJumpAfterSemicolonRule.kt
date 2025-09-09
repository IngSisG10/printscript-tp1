package linter.syntax.rules

import common.data.LinterData
import common.exception.NoNewLineAfterSemiColon
import common.token.EndSentenceToken
import common.token.NewLineToken
import common.token.abs.TokenInterface
import linter.syntax.LinterRule

class LineJumpAfterSemicolonRule : LinterRule {
    override fun getName(): String = "line_jump_after_semicolon"

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
