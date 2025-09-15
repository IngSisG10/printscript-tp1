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
        val out = mutableListOf<TokenInterface>()
        var newLine = false
        var i = 0

        while (i < tokens.size) {
            if (tokens[i] is EndSentenceToken) {
                out.add(tokens[i])
                // Añadir un salto de línea si no es el último token y el siguiente no es ya un salto de línea
                if (i + 1 < tokens.size && tokens[i + 1] !is NewLineToken) {
                    out.add(NewLineToken(0, 0))
                }
            } else {
                out.add(tokens[i])
            }
            i++
        }

        return out
    }
}
