package formatter.fixes.custom

import common.token.EndSentenceToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement

class SpaceAfterSemiColonFix : FormatterFix {
    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean =
        fixesIWantToApply.containsKey("space-after-semi-colon") &&
            fixesIWantToApply["space-after-semi-colon"]?.toString()?.toBoolean() == true

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val result = mutableListOf<TokenInterface>()

        for (i in tokens.indices) {
            val token = tokens[i]
            result.add(token)

            if (token is EndSentenceToken) {
                val nextToken = tokens.getOrNull(i + 1)
                if (nextToken !is WhiteSpaceToken) {
                    result.add(WhiteSpaceToken(1, 1))
                }
            }
        }
        return result
    }
}
