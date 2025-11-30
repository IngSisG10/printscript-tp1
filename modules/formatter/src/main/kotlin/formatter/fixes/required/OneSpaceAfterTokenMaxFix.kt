package formatter.fixes.required

import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement

class OneSpaceAfterTokenMaxFix : FormatterFix {
    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean =
        fixesIWantToApply.containsKey("mandatory-single-space-separation") &&
            fixesIWantToApply["mandatory-single-space-separation"]?.toString()?.toBoolean() == true

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()
        var index = 0

        while (index < mutableTokens.size) {
            val current = mutableTokens[index]

            if (current is WhiteSpaceToken) {
                var spaceCount = 0
                var j = index

                while (j < mutableTokens.size && mutableTokens[j] is WhiteSpaceToken) {
                    spaceCount++
                    j++
                }

                if (spaceCount > 1) {
                    for (k in 1 until spaceCount) {
                        mutableTokens.removeAt(index + 1)
                    }
                    return mutableTokens
                }

                index = j
            } else {
                index++
            }
        }
        return mutableTokens
    }

    override fun getFixNameAndValue(): formatter.dto.FormatterDTO =
        formatter.dto.FormatterDTO(
            name = "mandatory-single-space-separation",
            data =
                listOf(
                    formatter.dto.DataItem(
                        value = "activate",
                        default = "true",
                        type = "Boolean",
                    ),
                ),
        )
}
