package formatter.fixes.required

import common.enums.FunctionEnum
import common.token.EndSentenceToken
import common.token.FunctionToken
import common.token.NewLineToken
import common.token.abs.TokenInterface
import formatter.dto.FormatterDTO
import formatter.fixes.abs.FixSettings
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement

class LineJumpSpaceBeforePrintlnFix :
    FormatterFix,
    FixSettings {
    private var maxNewLines = 1
    private var afterPrintLn = false
    private var isNewLine = true

    override fun setFix(fixes: Map<String, JsonElement>) {
        maxNewLines = fixes["line-breaks-after-println"]?.toString()?.toIntOrNull() ?: 1
    }

    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean {
        val configValue = fixesIWantToApply["line-breaks-after-println"]?.toString()?.toIntOrNull()
        return configValue != null && configValue >= 0
    }

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val out = mutableListOf<TokenInterface>()
        var i = 0
        while (i < tokens.size) {
            while (tokens[i] is NewLineToken && afterPrintLn) {
                i++
            }
            if (afterPrintLn && isNewLine) {
                repeat(maxNewLines + 1) {
                    out.add(NewLineToken(0, 0))
                }
                afterPrintLn = false
            }
            if (tokens[i] is FunctionToken && tokens[i].value == FunctionEnum.PRINTLN) {
                afterPrintLn = true
                isNewLine = false
                out.add(tokens[i])
            } else if (tokens[i] is EndSentenceToken) {
                isNewLine = true
                out.add(tokens[i])
            } else {
                out.add(tokens[i])
            }
            i++
        }
        isNewLine = true
        return out
    }

    override fun getFixNameAndValue(): FormatterDTO =
        FormatterDTO(
            name = "line-breaks-after-println",
            data =
                listOf(
                    formatter.dto.DataItem(
                        value = "number of line breaks after println",
                        default = "$maxNewLines",
                        type = "Integer",
                    ),
                ),
        )
}
