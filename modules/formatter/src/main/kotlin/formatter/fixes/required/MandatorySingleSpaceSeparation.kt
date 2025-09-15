package formatter.fixes.required

import common.token.EndSentenceToken
import common.token.NewLineToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement

class MandatorySingleSpaceSeparation : FormatterFix {
    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean =
        fixesIWantToApply.containsKey("mandatory-single-space-separation") &&
            fixesIWantToApply["mandatory-single-space-separation"]?.toString()?.toBoolean() == true

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        // 1) Compactar: eliminar todos los WhiteSpaceToken
        val compact = ArrayList<TokenInterface>(tokens.size)
        for (t in tokens) {
            if (t !is WhiteSpaceToken) compact += t
        }

        // 2) Reconstruir insertando como mucho un espacio donde corresponde
        val out = ArrayList<TokenInterface>(compact.size * 2)
        for (i in compact.indices) {
            val cur = compact[i]
            out += cur

            if (i == compact.lastIndex) break
            val next = compact[i + 1]

            val needSpace =
                when {
                    // No espacio si cualquiera de los lados es un salto de línea
                    cur is NewLineToken -> false
                    next is NewLineToken -> false
                    // No espacio inmediatamente antes del fin de oración
                    next is EndSentenceToken -> false
                    // En cualquier otro caso, insertar un único espacio
                    else -> true
                }

            if (needSpace) out += WhiteSpaceToken(0, 0)
        }

        return out
    }
}
