package formatter.fixes

import common.data.LinterData
import common.exception.NoSpaceBeforeColonException
import common.token.TypeDeclaratorToken
import common.token.abs.TokenInterface
import formatter.FormatterFix

class SpaceBeforeColon : FormatterFix {
    override fun canFix(issue: common.data.LinterData): Boolean = issue.exception is common.exception.NoSpaceBeforeColonException

    // string -> FormattedCode (lista de tokens formateada)
    override fun fix(
        issue: common.data.LinterData,
        tokens: List<common.token.abs.TokenInterface>,
    ): List<common.token.abs.TokenInterface> {
        // utilizo index para trabajar dentro de la lista de tokens
        // recorro cada index e intento fixearlo
        // si lo fixeo, retorno el codigo formateado
        // si no, retorno un error
        // Si no puede fixearse debido a que cumple correctamente, retorno el codigo original

        val mutableTokens = tokens.toMutableList()

        for (i in 1 until mutableTokens.size) {
            val current = mutableTokens[i]

            // Encontramos un ":"
            if (current is common.token.TypeDeclaratorToken && current.value == ":") {
                val prev = mutableTokens[i - 1]

                // Si el token anterior NO es un Whitespace, insertamos uno
                if (prev !is common.token.WhiteSpaceToken) {
                    // fixme: los valores de row y position son arbitrarios
                    mutableTokens.add(i, common.token.WhiteSpaceToken(1, 2))
                    return mutableTokens
                }
            }
        }

        // Si no encontramos nada que arreglar, devolvemos la lista original
        return tokens
    }
}
