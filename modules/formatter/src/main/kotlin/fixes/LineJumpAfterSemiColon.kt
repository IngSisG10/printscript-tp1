package fixes

import FormatterFix
import data.LinterData
import token.abs.TokenInterface

class LineJumpAfterSemiColon : FormatterFix {
    override fun canFix(issue: LinterData): Boolean = issue.exception is SpaceAfterColon // todo: crear la excepción

    override fun fix(
        issue: LinterData,
        tokens: List<TokenInterface>,
    ): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in tokens.indices) {
            val current = tokens[i] // Recorremos los tokens

            if (current is token.EndSentenceToken && current.value == ";") {
                // todo: agregar la logica necesaria.
                // Debe haber un salto de línea a continuación de un “;”. No se configura.
            }
        }

        return tokens
    }
}
