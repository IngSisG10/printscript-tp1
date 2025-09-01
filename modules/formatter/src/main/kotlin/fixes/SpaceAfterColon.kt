package fixes

import FormatterFix
import token.abs.TokenInterface

class SpaceAfterColon : FormatterFix {
    override fun canFix(issue: LinterData): Boolean = issue.exception is NoSpaceAfterColonException

    override fun fix(
        issue: LinterData,
        tokens: List<TokenInterface>,
    ): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in 0 until mutableTokens.size - 1) {
            val current = mutableTokens[i]

            // Encontramos un ":"
            if (current is token.TypeDeclaratorToken && current.value == ":") {
                val next = mutableTokens[i + 1]

                // Si el token siguiente NO es un Whitespace, insertamos uno
                if (next !is token.WhitespaceToken) {
                    mutableTokens.add(i + 1, token.WhitespaceToken(" "))
                    return mutableTokens
                }
            }
        }

        // Si no encontramos nada que arreglar, devolvemos la lista original
        return tokens
    }
}
