package formatter.fixes.required

import common.token.EndSentenceToken
import common.token.NewLineToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement

class LineJumpAfterSemiColonFix : FormatterFix {
    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean =
        fixesIWantToApply.containsKey("mandatory-line-break-after-statement") &&
            fixesIWantToApply["mandatory-line-break-after-statement"]?.toString()?.toBoolean() == true

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in tokens.indices) {
            val current = tokens[i] // Recorremos los tokens

            if (current is EndSentenceToken && current.value == ";") {
                if (i + 1 < tokens.size) {
                    val next = tokens[i + 1]
                    if (next !is NewLineToken) {
                        // Insertamos un salto de línea después del punto y coma
                        mutableTokens.add(i + 1, NewLineToken(current.row, current.position + 1))
                    }
                } else {
                    // Si el punto y coma es el último token, simplemente añadimos un salto de línea al final
                    mutableTokens.add(NewLineToken(current.row, current.position + 1))
                }
                break // Salimos del bucle después de arreglar el primer punto y coma encontrado
            }
        }
        return mutableTokens
    }
}
