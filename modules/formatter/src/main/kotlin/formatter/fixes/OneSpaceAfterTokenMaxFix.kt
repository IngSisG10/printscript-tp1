package formatter.fixes

import common.data.LinterData
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import exception.MoreThanOneSpaceAfterTokenException
import formatter.FormatterFix

class OneSpaceAfterTokenMaxFix : FormatterFix {
    override fun canFix(issue: LinterData): Boolean = issue.exception is MoreThanOneSpaceAfterTokenException

    override fun fix(
        issue: LinterData,
        tokens: List<TokenInterface>,
    ): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()
        var index = 0

        while (index < mutableTokens.size) {
            val current = mutableTokens[index]

            if (current is WhiteSpaceToken) {
                var spaceCount = 0
                var j = index

                while (j < mutableTokens.size && mutableTokens[j] is WhiteSpaceToken) {
                    spaceCount++
                    j++
                }

                if (spaceCount > 1) {
                    for (k in 1 until spaceCount) {
                        mutableTokens.removeAt(index + 1)
                    }
                    return mutableTokens
                }

                index = j
            } else {
                index++
            }
        }
        return mutableTokens
    }
}
