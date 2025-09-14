package formatter.fixes.required

import common.token.TypeDeclaratorToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix

class SpaceBeforeColonFix : FormatterFix {
    override fun getName(): String = "space_before_colon_fix"

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in 1 until mutableTokens.size) {
            val current = mutableTokens[i]

            if (current is TypeDeclaratorToken) {
                val prev = mutableTokens[i - 1]

                // Si el token anterior NO es un Whitespace, insertamos uno
                if (prev !is WhiteSpaceToken) {
                    mutableTokens.add(i, WhiteSpaceToken(current.row, current.position - 1))
                    return mutableTokens
                }
            }
        }

        return tokens
    }
}
