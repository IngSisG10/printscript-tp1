package formatter.fixes

import common.data.LinterData
import common.enums.FunctionEnum
import common.exception.InvalidNewLineBeforePrintlnException
import common.token.FunctionToken
import common.token.NewLineToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix

class LineJumpSpaceBeforePrintlnFix(
    private val maxNewLines: Int = 2, // configurable, default = 2
) : FormatterFix {
    override fun canFix(issue: LinterData): Boolean = issue.exception is InvalidNewLineBeforePrintlnException

    override fun fix(
        issue: LinterData,
        tokens: List<TokenInterface>,
    ): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in mutableTokens.indices) {
            val current = mutableTokens[i]

            if (current is FunctionToken && current.value == FunctionEnum.PRINTLN) {
                // Count consecutive newlines before println
                var newLineCount = 0
                var j = i - 1
                while (j >= 0 && mutableTokens[j] is NewLineToken) {
                    newLineCount++
                    j--
                }

                // Too many newlines? Trim to maxNewLines
                if (newLineCount > maxNewLines) {
                    val toRemove = newLineCount - maxNewLines
                    repeat(toRemove) {
                        mutableTokens.removeAt(i - newLineCount)
                    }
                }

                // stop after fixing first println (or remove `return` if you want to fix all)
                return mutableTokens
            }
        }

        return mutableTokens
    }
}
