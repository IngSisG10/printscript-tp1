package formatter.fixes.required

import common.enums.OperationEnum
import common.token.OperationToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix

class SpaceAfterEqualFix : FormatterFix {
    override fun getName(): String = "space_after_equal_fix"

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in 1 until mutableTokens.size) {
            val current = mutableTokens[i]

            if (current is OperationToken && current.value == OperationEnum.EQUAL) {
                val next = mutableTokens[i + 1]

                if (next !is WhiteSpaceToken) {
                    mutableTokens.add(i + 1, WhiteSpaceToken(current.row, current.position + 1))
                }
            }
        }

        return mutableTokens
    }
}
