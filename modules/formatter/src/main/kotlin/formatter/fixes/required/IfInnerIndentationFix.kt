package formatter.fixes.required

import common.token.CloseBraceToken
import common.token.NewLineToken
import common.token.OpenBraceToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.dto.FormatterDTO
import formatter.fixes.abs.FixSettings
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement

class IfInnerIndentationFix :
    FormatterFix,
    FixSettings {
    private var n = 2

    override fun setFix(fixes: Map<String, JsonElement>) {
        n = fixes["indent-inside-if"]?.toString()?.toIntOrNull() ?: 2
    }

    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean {
        val configValue = fixesIWantToApply["indent-inside-if"]?.toString()?.toIntOrNull()
        return configValue != null && configValue > 0
    }

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

    override fun getFixNameAndValue(): FormatterDTO =
        FormatterDTO(
            name = "indent-inside-if",
            data =
                listOf(
                    formatter.dto.DataItem(
                        value = "number of spaces",
                        default = "$n",
                        type = "Integer",
                    ),
                ),
        )

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
