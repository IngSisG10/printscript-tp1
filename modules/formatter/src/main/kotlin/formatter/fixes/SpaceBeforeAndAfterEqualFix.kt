package formatter.fixes

import common.enums.OperationEnum
import common.token.OperationToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix

class SpaceBeforeAndAfterEqualFix : FormatterFix {
    override fun getName(): String = "space_before_and_after_equal_fix"

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (index in 1 until mutableTokens.size) {
            val current = mutableTokens[index]

            if (current is OperationToken && current.value == OperationEnum.EQUAL) {
                val prev = mutableTokens[index - 1]

                var insertedBefore = false

                if (prev !is WhiteSpaceToken) {
                    mutableTokens.add(index, WhiteSpaceToken(1, 2))
                    insertedBefore = true
                }

                val equalsIndex = if (insertedBefore) index + 1 else index
                val nextToken = mutableTokens[equalsIndex + 1]

                if (nextToken !is WhiteSpaceToken) {
                    mutableTokens.add(equalsIndex + 1, WhiteSpaceToken(1, 2))
                }
            }
        }
        return mutableTokens
    }
}
