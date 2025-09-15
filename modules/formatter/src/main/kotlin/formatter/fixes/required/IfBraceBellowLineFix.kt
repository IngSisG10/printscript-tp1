package formatter.fixes.required

import common.token.CloseParenthesisToken
import common.token.IfToken
import common.token.NewLineToken
import common.token.OpenBraceToken
import common.token.abs.TokenInterface
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
                // Find the closing parenthesis after "if token"
                var closeParenIndex = -1
                for (j in i + 1 until mutableTokens.size) {
                    if (mutableTokens[j] is CloseParenthesisToken) {
                        closeParenIndex = j
                        break
                    }
                }

                if (closeParenIndex != -1) {
                    // Ensure a NewLineToken is added after the closing parenthesis
                    val nextIndex = closeParenIndex + 1
                    if (nextIndex < mutableTokens.size && mutableTokens[nextIndex] !is NewLineToken) {
                        mutableTokens.add(nextIndex, NewLineToken(0, 0))
                    }

                    // Ensure the opening brace is on the next line
                    var openBraceIndex = -1
                    for (j in nextIndex until mutableTokens.size) {
                        if (mutableTokens[j] is OpenBraceToken) {
                            openBraceIndex = j
                            break
                        }
                    }

                    if (openBraceIndex != -1 && openBraceIndex > nextIndex + 1) {
                        mutableTokens.removeAt(openBraceIndex)
                        mutableTokens.add(nextIndex + 1, OpenBraceToken(0, 0))
                    }
                }
            }
            i++
        }
        return mutableTokens
    }
}
