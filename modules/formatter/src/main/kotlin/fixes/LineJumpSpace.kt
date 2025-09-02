package fixes

import FormatterFix
import data.LinterData
import enums.FunctionEnum

class LineJumpSpace : FormatterFix {
    override fun canFix(issue: LinterData): Boolean = issue.exception is SpaceAfterColon

    // todo: modificarlo para que un futuro pueda especificar cuántos espacios quiere.

    override fun fix(
        issue: LinterData,
        tokens: List<token.abs.TokenInterface>,
    ): List<token.abs.TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in tokens.indices) {
            val current = tokens[i]

            if (current is token.FunctionToken && current.value == FunctionEnum.PRINTLN) { // Tenemos un token "println" ?

                var newLineCount = 0
                var j = i - 1

                while (j >= 0 && tokens[j] is token.WhiteSpaceToken) { // Contamos los saltos de línea antes del "println", hay que crear un
                    // token NewLineToken (?)
                    newLineCount++
                    j--
                }

                if (newLineCount > 2) {
                    val toRemove = newLineCount - 2
                    repeat(toRemove) {
                        mutableTokens.removeAt(i - newLineCount)
                    }
                }
                return mutableTokens
            }
        }
        return tokens
    }
}
