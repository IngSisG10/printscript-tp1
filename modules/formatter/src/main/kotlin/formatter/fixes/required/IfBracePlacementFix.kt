package formatter.fixes.required

import common.token.CloseParenthesisToken
import common.token.IfToken
import common.token.NewLineToken
import common.token.OpenBraceToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix

class IfBracePlacementFix : FormatterFix {
    override fun getName(): String = "if_brace_placement_fix"

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

                if (closeParenIndex != -1 && closeParenIndex + 2 < mutableTokens.size) {
                    val nextToken = mutableTokens[closeParenIndex + 1]
                    val tokenAfterNext = mutableTokens[closeParenIndex + 2]

                    // Check if there's a newline between ) and {
                    if (nextToken is NewLineToken && tokenAfterNext is OpenBraceToken) {
                        // Remove the newline token to put brace on same line
                        mutableTokens.removeAt(closeParenIndex + 1)
                        // Don't increment i since we removed a token
                        continue
                    }
                }
            }
            i++
        }

        return mutableTokens
    }
}
