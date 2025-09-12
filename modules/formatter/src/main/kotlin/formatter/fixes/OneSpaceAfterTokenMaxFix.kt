package formatter.fixes

import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix

class OneSpaceAfterTokenMaxFix : FormatterFix {
    override fun getName(): String = "one_space_after_token_max_fix"

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
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
