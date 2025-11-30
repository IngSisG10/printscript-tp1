package formatter.fixes.required

import common.token.TypeDeclaratorToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.dto.FormatterDTO
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement

class SpaceBeforeColonFix : FormatterFix {
    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean =
        fixesIWantToApply.containsKey("enforce-spacing-before-colon-in-declaration") &&
            fixesIWantToApply["enforce-spacing-before-colon-in-declaration"]?.toString()?.toBoolean() == true

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in 1 until mutableTokens.size) {
            val current = mutableTokens[i]

            if (current is TypeDeclaratorToken) {
                val prev = mutableTokens[i - 1]

                // Si el token anterior NO es un Whitespace, insertamos uno
                if (prev !is WhiteSpaceToken) {
                    mutableTokens.add(i, WhiteSpaceToken(current.row, current.position - 1))
                    return mutableTokens
                }
            }
        }

        return tokens
    }

    override fun getFixNameAndValue(): FormatterDTO =
        FormatterDTO(
            name = "enforce-spacing-before-colon-in-declaration",
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
