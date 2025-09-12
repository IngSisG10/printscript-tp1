package formatter.rules

import common.data.FormatterData
import common.enums.FunctionEnum
import common.exception.InvalidNewLineBeforePrintlnException
import common.token.FunctionToken
import common.token.NewLineToken
import common.token.abs.TokenInterface
import formatter.rules.abs.FormatterRule

class NewLineBeforePrintLnRule(
    private val allowedNewLines: Int = 1, // configurable: 0, 1 o 2
) : FormatterRule {
    override fun getName(): String = "new_line_before_println_rule"

    override fun matchWithData(tokens: List<TokenInterface>): List<FormatterData> {
        val list = mutableListOf<FormatterData>()
        for ((index, token) in tokens.withIndex()) {
            if (token is FunctionToken && token.value == FunctionEnum.PRINTLN) {
                val newLines = countConsecutiveNewLines(tokens, index - 1)
                if (newLines != allowedNewLines) {
                    list.add(
                        FormatterData(
                            exception = InvalidNewLineBeforePrintlnException(expected = allowedNewLines, found = newLines),
                            position = index,
                        ),
                    )
                }
            }
        }
        return list
    }

    private fun countConsecutiveNewLines(
        tokens: List<TokenInterface>,
        startIndex: Int,
    ): Int {
        var count = 0
        var i = startIndex
        while (i >= 0 && tokens[i] is NewLineToken) {
            count++
            i--
        }
        return count
    }
}
