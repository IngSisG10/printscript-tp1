package linter.syntax.rules

import common.data.LinterData
import common.enums.FunctionEnum
import common.exception.InvalidNewLineBeforePrintlnException
import common.token.FunctionToken
import common.token.NewLineToken
import common.token.abs.TokenInterface
import linter.syntax.LinterRule

class NewLineBeforePrintlnRule(
    private val allowedNewLines: Int = 1, // configurable: 0, 1 o 2
) : LinterRule {
    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun match(tokens: List<TokenInterface>): Exception? {
        for ((index, token) in tokens.withIndex()) {
            if (token is FunctionToken && token.value == FunctionEnum.PRINTLN) {
                val newLines = countConsecutiveNewLines(tokens, index - 1)
                if (newLines != allowedNewLines) {
                    return InvalidNewLineBeforePrintlnException(expected = allowedNewLines, found = newLines)
                }
            }
        }
        return null
    }

    override fun matchWithData(tokens: List<TokenInterface>): List<LinterData> {
        val list = mutableListOf<LinterData>()
        for ((index, token) in tokens.withIndex()) {
            if (token is FunctionToken && token.value == FunctionEnum.PRINTLN) {
                val newLines = countConsecutiveNewLines(tokens, index - 1)
                if (newLines != allowedNewLines) {
                    list.add(
                        LinterData(
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
