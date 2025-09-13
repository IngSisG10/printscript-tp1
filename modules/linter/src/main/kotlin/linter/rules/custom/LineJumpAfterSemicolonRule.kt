package linter.rules.custom

import common.exception.NoNewLineAfterSemiColon
import common.token.EndSentenceToken
import common.token.NewLineToken
import common.token.abs.TokenInterface
import linter.rules.abs.LinterRule

class LineJumpAfterSemicolonRule : LinterRule {
    override fun getName(): String = "line_jump_after_semicolon"

    override fun match(tokens: List<TokenInterface>): List<Throwable> {
        val list = mutableListOf<Throwable>()
        for ((index, token) in tokens.withIndex()) {
            if (token is EndSentenceToken) {
                val next = tokens.getOrNull(index + 1)

                if (next !is NewLineToken) {
                    list.add(NoNewLineAfterSemiColon(token.getPosition()))
                }
            }
        }
        return list.toList()
    }
}
