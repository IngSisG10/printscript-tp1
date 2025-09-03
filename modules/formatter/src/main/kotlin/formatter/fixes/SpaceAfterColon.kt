package formatter.fixes

import common.data.LinterData
import common.exception.NoSpaceAfterColonException
import common.token.abs.TokenInterface
import formatter.FormatterFix

class SpaceAfterColon : FormatterFix {
    override fun canFix(issue: common.data.LinterData): Boolean = issue.exception is common.exception.NoSpaceAfterColonException

    override fun fix(
        issue: common.data.LinterData,
        tokens: List<common.token.abs.TokenInterface>,
    ): List<common.token.abs.TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in 0 until mutableTokens.size - 1) {
            val current = mutableTokens[i]

            // Encontramos un ":"
            if (current is common.token.TypeDeclaratorToken && current.value == ":") {
                val next = mutableTokens[i + 1]

                // Si el token siguiente NO es un Whitespace, insertamos uno
                if (next !is common.token.WhiteSpaceToken) {
                    // fixme: los valores de row y position son arbitrarios
                    mutableTokens.add(i + 1, common.token.WhiteSpaceToken(1, 2))
                    return mutableTokens
                }
            }
        }

        // Si no encontramos nada que arreglar, devolvemos la lista original
        return tokens
    }
}
