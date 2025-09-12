package formatter.fixes

import common.enums.OperationEnum
import common.token.OperationToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix

class SpaceBeforeEqualFix : FormatterFix {
    override fun getName(): String = "space_before_equal_fix"

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in 1 until mutableTokens.size) {
            val current = mutableTokens[i]

            if (current is OperationToken && current.value == OperationEnum.EQUAL) {
                val prev = mutableTokens[i - 1]

                if (prev !is common.token.WhiteSpaceToken) {
                    mutableTokens.add(i, common.token.WhiteSpaceToken(current.row, current.position - 1))
                }
            }
        }

        return mutableTokens
    }
}
