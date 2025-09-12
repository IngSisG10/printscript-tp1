package linter.rules.custom

import common.enums.FunctionEnum
import common.exception.InvalidNewLineBeforePrintlnException
import common.token.FunctionToken
import common.token.NewLineToken
import common.token.abs.TokenInterface
import linter.rules.abs.LinterRule

class NewLineBeforePrintlnRule(
    private val allowedNewLines: Int = 1, // configurable: 0, 1 o 2
) : LinterRule {
    override fun getName(): String = "new_line_before_println"

    override fun match(tokens: List<TokenInterface>): List<Throwable> {
        val list = mutableListOf<Throwable>()
        for ((index, token) in tokens.withIndex()) {
            if (token is FunctionToken && token.value == FunctionEnum.PRINTLN) {
                val newLines = countConsecutiveNewLines(tokens, index - 1)
                if (newLines != allowedNewLines) {
                    list.add(InvalidNewLineBeforePrintlnException(expected = allowedNewLines, found = newLines))
                }
            }
        }
        return list.toList()
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
