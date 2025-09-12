package formatter.fixes

import common.token.OperationToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix

class SpaceBeforeOperatorFix : FormatterFix {
    override fun getName(): String = "space_before_operator_fix"

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in 1 until mutableTokens.size) {
            val current = mutableTokens[i]

            if (current is OperationToken) {
                val prev = mutableTokens[i - 1]

                if (prev !is WhiteSpaceToken) {
                    mutableTokens.add(i, WhiteSpaceToken(current.row, current.position - 1))
                }
            }
        }

        return mutableTokens
    }
}
