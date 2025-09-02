package fixes

import FormatterFix
import enums.OperationEnum
import token.OperationToken
import token.abs.TokenInterface

class SpaceBeforeAndAfterEqual : FormatterFix {
    override fun canFix(issue: data.LinterData): Boolean =
        issue.exception is SpaceBeforeAndAfterEqual // Por el momento voy a crear uno, en caso
    // de que haya un problema lo separo en antes y despues.

    override fun fix(
        issue: data.LinterData,
        tokens: List<TokenInterface>,
    ): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (index in 1 until mutableTokens.size) {
            val current = mutableTokens[index]

            if (current is OperationToken && current.value == OperationEnum.EQUAL) {
                val prev = mutableTokens[index - 1]

                var insertedBefore = false

                if (prev !is token.WhiteSpaceToken) {
                    mutableTokens.add(index, token.WhiteSpaceToken(1, 2))
                    insertedBefore = true
                }

                val equalsIndex = if (insertedBefore) index + 1 else index
                val nextToken = mutableTokens[equalsIndex + 1]

                if (nextToken !is token.WhiteSpaceToken) {
                    mutableTokens.add(equalsIndex + 1, token.WhiteSpaceToken(1, 2))
                }
            }
            return mutableTokens
        }
        return tokens
    }
}
