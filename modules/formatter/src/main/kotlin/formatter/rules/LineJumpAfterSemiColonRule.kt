package formatter.rules

import common.data.FormatterData
import common.exception.NoNewLineAfterSemiColon
import common.token.EndSentenceToken
import common.token.NewLineToken
import common.token.abs.TokenInterface
import formatter.rules.abs.FormatterRule

class LineJumpAfterSemiColonRule : FormatterRule {
    override fun getName(): String = "line_jump_after_semicolon"

    override fun matchWithData(tokens: List<TokenInterface>): List<FormatterData> {
        val list = mutableListOf<FormatterData>()
        for ((index, token) in tokens.withIndex()) {
            if (token is EndSentenceToken) {
                val next = tokens.getOrNull(index + 1)
                if (next !is NewLineToken) {
                    list.add(
                        FormatterData(
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
