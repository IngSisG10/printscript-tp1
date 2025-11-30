package formatter.fixes.required

import common.token.CloseParenthesisToken
import common.token.IfToken
import common.token.OpenBraceToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.dto.FormatterDTO
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement

class IfBraceSameLinePlacementFix : FormatterFix {
    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean =
        fixesIWantToApply.containsKey("if-brace-same-line") &&
            fixesIWantToApply["if-brace-same-line"]?.toString()?.toBoolean() == true

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        var i = 0
        while (i < mutableTokens.size) {
            val token = mutableTokens[i]

            if (token is IfToken) {
                // Find the closing parenthesis after "if token"
                var closeParenIndex = -1
                for (j in i + 1 until mutableTokens.size) {
                    if (mutableTokens[j] is CloseParenthesisToken) {
                        closeParenIndex = j
                        break
                    }
                }

                if (closeParenIndex != -1) {
                    // Delete all tokens after ) until we find {
                    val currentIndex = closeParenIndex + 1
                    while (currentIndex < mutableTokens.size && mutableTokens[currentIndex] !is OpenBraceToken) {
                        mutableTokens.removeAt(currentIndex)
                    }

                    // Add a space between ) and {
                    val spaceToken =
                        WhiteSpaceToken(
                            tokens[closeParenIndex].getPosition().row,
                            tokens[closeParenIndex].getPosition().pos,
                        )
                    mutableTokens.add(closeParenIndex + 1, spaceToken)
                }
            }
            i++
        }

        return mutableTokens
    }

    override fun getFixNameAndValue(): FormatterDTO =
        FormatterDTO(
            name = "if-brace-same-line",
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
