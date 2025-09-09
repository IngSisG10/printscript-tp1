package formatter.fixes

import common.data.LinterData
import common.exception.NoSpaceBeforeColonException
import common.token.TypeDeclaratorToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix

class SpaceBeforeColonFix : FormatterFix {
    override fun canFix(issue: LinterData): Boolean = issue.exception is NoSpaceBeforeColonException

    // string -> FormattedCode (lista de tokens formateada)
    override fun fix(
        issue: LinterData,
        tokens: List<TokenInterface>,
    ): List<TokenInterface> {
        // utilizo index para trabajar dentro de la lista de tokens
        // recorro cada index e intento fixearlo
        // si lo fixeo, retorno el codigo formateado
        // si no, retorno un error
        // Si no puede fixearse debido a que cumple correctamente, retorno el codigo original

        val mutableTokens = tokens.toMutableList()

        for (i in 1 until mutableTokens.size) {
            val current = mutableTokens[i]

            // Encontramos un ":"
            if (current is TypeDeclaratorToken && current.value == ":") {
                val prev = mutableTokens[i - 1]

                // Si el token anterior NO es un Whitespace, insertamos uno
                if (prev !is WhiteSpaceToken) {
                    // fixme: los valores de row y position son arbitrarios
                    mutableTokens.add(i, WhiteSpaceToken(1, 2))
                    return mutableTokens
                }
            }
        }

        // Si no encontramos nada que arreglar, devolvemos la lista original
        return tokens
    }
}
