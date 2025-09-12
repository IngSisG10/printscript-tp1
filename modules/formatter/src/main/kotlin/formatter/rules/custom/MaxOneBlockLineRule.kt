package formatter.rules.custom

import common.data.FormatterData
import common.exception.TooManyBlankLinesException
import common.token.NewLineToken
import common.token.abs.TokenInterface
import formatter.rules.abs.FormatterRule

class MaxOneBlockLineRule : FormatterRule {
    override fun getName(): String = "max_one_block_line_rule"

    override fun matchWithData(tokens: List<TokenInterface>): List<FormatterData> {
        val issues = mutableListOf<FormatterData>()
        var blankLineCount = 0

        for ((index, token) in tokens.withIndex()) {
            if (token is NewLineToken) {
                blankLineCount++
                if (blankLineCount > 2) {
                    issues.add(
                        FormatterData(
                            exception = TooManyBlankLinesException("Too many blank lines at index $index"),
                            position = index,
                        ),
                    )
                }
            } else {
                blankLineCount = 0
            }
        }
        return issues
    }
}
