package formatter.fixes.required

import common.token.NewLineToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement

class LineJumpAfterSemiColonFix : FormatterFix {
    private var first = true

    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean =
        fixesIWantToApply.containsKey("mandatory-line-break-after-statement") &&
            fixesIWantToApply["mandatory-line-break-after-statement"]?.toString()?.toBoolean() == true

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val out = mutableListOf<TokenInterface>()
        var i = 0
        while (i < tokens.size) {
            if (tokens[i] is NewLineToken && first) {
//                i++
            } else if (i == 0 && tokens[i] !is NewLineToken && !first) {
                out.add(NewLineToken(0, 0))
                out.add(tokens[i])
            } else {
                out.add(tokens[i])
            }
            first = false
            i++
        }
        return out
    }
}
