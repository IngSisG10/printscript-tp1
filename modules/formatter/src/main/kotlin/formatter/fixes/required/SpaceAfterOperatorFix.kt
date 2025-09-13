package formatter.fixes.required

import common.token.OperationToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix

class SpaceAfterOperatorFix : FormatterFix {
    override fun getName(): String = "space_after_operator_fix"

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in 1 until mutableTokens.size) {
            val current = mutableTokens[i]

            if (current is OperationToken) {
                val next = mutableTokens[i + 1]

                if (next !is WhiteSpaceToken) {
                    mutableTokens.add(i + 1, WhiteSpaceToken(current.row, current.position + 1))
                }
            }
        }

        return mutableTokens
    }
}
