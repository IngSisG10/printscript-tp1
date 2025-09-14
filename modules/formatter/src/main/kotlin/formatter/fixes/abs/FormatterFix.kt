package formatter.fixes.abs

import common.token.abs.TokenInterface
import kotlinx.serialization.json.JsonElement

interface FormatterFix {
    fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean

    fun fix(tokens: List<TokenInterface>): List<TokenInterface>
}
