package formatter.fixes

import common.data.LinterData
import common.exception.NoNewLineAfterSemiColon
import common.token.EndSentenceToken
import common.token.NewLineToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix

class LineJumpAfterSemiColonFix : FormatterFix {
    override fun canFix(issue: LinterData): Boolean = issue.exception is NoNewLineAfterSemiColon

    override fun fix(
        issue: LinterData,
        tokens: List<TokenInterface>,
    ): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in tokens.indices) {
            val current = tokens[i] // Recorremos los tokens

            if (current is EndSentenceToken && current.value == ";") {
                if (i + 1 < tokens.size) {
                    val next = tokens[i + 1]
                    if (next !is NewLineToken) {
                        // Insertamos un salto de línea después del punto y coma
                        mutableTokens.add(i + 1, NewLineToken(1, 2))
                    }
                } else {
                    // Si el punto y coma es el último token, simplemente añadimos un salto de línea al final
                    mutableTokens.add(NewLineToken(1, 2))
                }
                break // Salimos del bucle después de arreglar el primer punto y coma encontrado
            }
        }
        return tokens
    }
} // fixme: Esto esta bien ?
