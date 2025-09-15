package formatter.fixes.required

import common.enums.FunctionEnum
import common.token.EndSentenceToken
import common.token.FunctionToken
import common.token.NewLineToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FixSettings
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

class LineJumpSpaceBeforePrintlnFix :
    FormatterFix,
    FixSettings {
    private var maxNewLines = 1
    private var afterPrintLn = false
    private var isNewLine = true

    override fun setFix(fixes: Map<String, JsonElement>) {
        // CAMBIO: parseo robusto
        maxNewLines = fixes["line-breaks-after-println"]?.jsonPrimitive?.intOrNull ?: 1
        if (maxNewLines < 0) maxNewLines = 0
    }

    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean {
        // CAMBIO: parseo robusto
        val configValue = fixesIWantToApply["line-breaks-after-println"]?.jsonPrimitive?.intOrNull
        return configValue != null && configValue >= 0
    }

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val out = mutableListOf<TokenInterface>()
        var i = 0
        while (i < tokens.size) {
            // CAMBIO 1: evitar "salto doble" del índice
            if (afterPrintLn && tokens[i] is NewLineToken) {
                i++
                continue
            }

            // CAMBIO 2: manejar primero el ';' y reintentar la iteración
            if (tokens[i] is EndSentenceToken) {
                isNewLine = true
                out.add(tokens[i])
                i++
                // Importante: reentrar al loop para evaluar (afterPrintLn && isNewLine)
                continue
            }

            if (tokens[i] is FunctionToken && tokens[i].value == FunctionEnum.PRINTLN) {
                afterPrintLn = true
                isNewLine = false
                out.add(tokens[i])
            } else if (afterPrintLn && isNewLine) {
                // ahora sí entra acá
                repeat(maxNewLines + 1) {
                    out.add(NewLineToken(0, 0))
                }
                afterPrintLn = false
            } else {
                out.add(tokens[i])
            }

            i++
        }
        isNewLine = true
        return out
    }
}
