package formatter.rules

import common.data.FormatterData
import common.exception.InvalidIfBracePlacementException
import common.token.CloseParenthesisToken
import common.token.IfToken
import common.token.NewLineToken
import common.token.OpenBraceToken
import common.token.abs.TokenInterface
import formatter.rules.abs.FormatterRule

class IfBracePlacementRule : FormatterRule {
    override fun getName(): String = "if_brace_placement_rule"

    override fun matchWithData(tokens: List<TokenInterface>): List<FormatterData> {
        val issues = mutableListOf<FormatterData>()

        for ((index, token) in tokens.withIndex()) {
            if (token is IfToken) {
                val closeParenthesisIndex = tokens.indexOfFirst { it is CloseParenthesisToken }
                if (closeParenthesisIndex != -1) {
                    val nextNewLine = tokens[closeParenthesisIndex + 1]
                    val nextOpenBrace = tokens[closeParenthesisIndex + 2]

                    if (nextNewLine is NewLineToken && nextOpenBrace is OpenBraceToken) {
                        issues.add(
                            FormatterData(
                                exception =
                                    InvalidIfBracePlacementException(
                                        "Opening brace should be on the same line as the if statement at index $index",
                                    ),
                                position = closeParenthesisIndex + 1, // Position of the newline token
                            ),
                        )
                    }
                }
            }
        }
        return issues
    }
}
