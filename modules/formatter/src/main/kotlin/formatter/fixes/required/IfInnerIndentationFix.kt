package formatter.fixes.required

import common.token.CloseBraceToken
import common.token.NewLineToken
import common.token.OpenBraceToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix

class IfInnerIndentationFix(
    private val n: Int,
) : FormatterFix {
    override fun getName(): String = "if_white_space_token_fix"

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val result = mutableListOf<TokenInterface>()
        var currentDepth = 0
        var i = 0

        while (i < tokens.size) {
            val token = tokens[i]

            when (token) {
                is OpenBraceToken -> {
                    result.add(token)
                    currentDepth++
                    i++
                }

                is CloseBraceToken -> {
                    currentDepth = maxOf(0, currentDepth - 1)
                    result.add(token)
                    i++
                }

                is NewLineToken -> {
                    result.add(token)
                    i++
                    // Fix indentation after newline
                    i = fixContentIndentation(tokens, i, currentDepth, result)
                }

                else -> {
                    result.add(token)
                    i++
                }
            }
        }

        return result
    }

    private fun fixContentIndentation(
        tokens: List<TokenInterface>,
        startIndex: Int,
        depth: Int,
        result: MutableList<TokenInterface>,
    ): Int {
        var i = startIndex

        // Skip existing whitespace tokens
        while (i < tokens.size && tokens[i] is WhiteSpaceToken) {
            i++
        }

        // Add correct indentation based on what comes after the newline
        if (i < tokens.size) {
            val expectedSpaces =
                if (tokens[i] is CloseBraceToken) {
                    // Closing braces get indented one level less than current depth
                    maxOf(0, (depth - 1) * n)
                } else {
                    // Regular content gets indented at current depth
                    depth * n
                }

            repeat(expectedSpaces) {
                val referenceToken = tokens[i]
                result.add(WhiteSpaceToken(referenceToken.row, referenceToken.position))
            }
        }

        return i
    }
}
