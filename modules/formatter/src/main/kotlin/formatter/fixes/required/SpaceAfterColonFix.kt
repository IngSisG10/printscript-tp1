package formatter.fixes.required

import common.token.TypeDeclaratorToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.dto.FormatterDTO
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement

class SpaceAfterColonFix : FormatterFix {
    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean =
        fixesIWantToApply.containsKey("enforce-spacing-after-colon-in-declaration") &&
            fixesIWantToApply["enforce-spacing-after-colon-in-declaration"]?.toString()?.toBoolean() == true

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in 0 until mutableTokens.size - 1) {
            val current = mutableTokens[i]

            if (current is TypeDeclaratorToken && current.value == ":") {
                val next = mutableTokens[i + 1]

                // Si el token siguiente NO es un Whitespace, insertamos uno
                if (next !is WhiteSpaceToken) {
                    mutableTokens.add(i + 1, WhiteSpaceToken(current.row, current.position + 1))
                    return mutableTokens
                }
            }
        }

        return tokens
    }

    override fun getFixNameAndValue(): FormatterDTO =
        FormatterDTO(
            name = "enforce-spacing-after-colon-in-declaration",
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
