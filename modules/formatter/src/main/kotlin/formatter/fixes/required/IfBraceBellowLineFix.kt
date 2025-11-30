package formatter.fixes.required

import common.token.CloseParenthesisToken
import common.token.IfToken
import common.token.NewLineToken
import common.token.OpenBraceToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.dto.FormatterDTO
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement

class IfBraceBellowLineFix : FormatterFix {
    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean =
        fixesIWantToApply.containsKey("if-brace-below-line") &&
            fixesIWantToApply["if-brace-below-line"]?.toString()?.toBoolean() == true

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        var i = 0
        while (i < mutableTokens.size) {
            val token = mutableTokens[i]

            if (token is IfToken) {
                var closeParenIndex = -1
                for (j in i + 1 until mutableTokens.size) {
                    if (mutableTokens[j] is CloseParenthesisToken) {
                        closeParenIndex = j
                        break
                    }
                }

                if (closeParenIndex != -1) {
                    val nextIndex = closeParenIndex + 1

                    if (nextIndex < mutableTokens.size && mutableTokens[nextIndex] !is NewLineToken) {
                        mutableTokens.add(nextIndex, NewLineToken(0, 0))
                    }

                    var openBraceIndex = -1
                    for (j in nextIndex until mutableTokens.size) {
                        if (mutableTokens[j] is OpenBraceToken) {
                            openBraceIndex = j
                            break
                        }
                    }

                    if (openBraceIndex != -1) {
                        if (openBraceIndex > nextIndex + 1) {
                            val brace = mutableTokens.removeAt(openBraceIndex)
                            mutableTokens.add(nextIndex + 1, brace)
                            openBraceIndex = nextIndex + 1
                        }

                        var k = openBraceIndex + 1
                        while (k < mutableTokens.size && mutableTokens[k] is WhiteSpaceToken) {
                            mutableTokens.removeAt(k)
                        }
                        if (k >= mutableTokens.size || mutableTokens[k] !is NewLineToken) {
                            mutableTokens.add(k, NewLineToken(0, 0))
                        }
                    }
                }
            }
            i++
        }
        return mutableTokens
    }

    override fun getFixNameAndValue(): FormatterDTO =
        FormatterDTO(
            name = "if-brace-below-line",
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
