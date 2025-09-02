package syntax.rules

import data.LinterData
import enums.FunctionEnum
import exception.InvalidNewLineBeforePrintlnException
import syntax.LinterRule
import token.FunctionToken
import token.abs.TokenInterface

class NewLineBeforePrintlnRule(
    private val allowedNewLines: Int = 1, // configurable: 0, 1 o 2
) : LinterRule {
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

    // todo: revisar esta logica
    private fun countConsecutiveNewLines(
        tokens: List<TokenInterface>,
        startIndex: Int,
    ): Int {
//        var count = 0
//        var i = startIndex
//        while (i >= 0 && tokens[i] is NewLineToken) {
//            count++
//            i--
//        }
//        return count
        TODO()
    }
}
