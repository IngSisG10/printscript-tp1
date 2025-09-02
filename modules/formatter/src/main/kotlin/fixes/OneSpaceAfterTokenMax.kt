package fixes

import FormatterFix
import data.LinterData
import token.abs.TokenInterface

// Debe haber un solo espacio, como máximo, entre los distintos “tokens”. No se
// configura.
class OneSpaceAfterTokenMax : FormatterFix {
    override fun canFix(issue: LinterData): Boolean = issue.exception is SpaceAfterColon

    override fun fix(
        issue: LinterData,
        tokens: List<TokenInterface>,
    ): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()
        var index = 0

        while (index < mutableTokens.size) { // Recorremos los tokens

            val current = mutableTokens[index]

            if (current is token.WhiteSpaceToken) {
                var spaceCount = 0
                var j = index

                while (j < mutableTokens.size && mutableTokens[j] is token.WhiteSpaceToken) {
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
