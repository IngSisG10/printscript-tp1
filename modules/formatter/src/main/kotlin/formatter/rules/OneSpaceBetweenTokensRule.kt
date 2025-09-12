package formatter.rules

import common.data.FormatterData
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import exception.MoreThanOneSpaceAfterTokenException
import formatter.rules.abs.FormatterRule

class OneSpaceBetweenTokensRule: FormatterRule {
    override fun getName(): String = "one_space_between_tokens_rule"


    override fun matchWithData(tokens: List<TokenInterface>): List<FormatterData> {
        val issues = mutableListOf<FormatterData>()
        var i = 0
        while (i < tokens.size - 1) {
            if (tokens[i] is WhiteSpaceToken && tokens[i + 1] is WhiteSpaceToken) {
                issues.add(
                    FormatterData(
                        exception = MoreThanOneSpaceAfterTokenException("More than one space between tokens at index $i"),
                        position = i,
                    ),
                )
                // Skip to the end of the whitespace sequence
                while (i < tokens.size - 1 && tokens[i + 1] is WhiteSpaceToken) {
                    i++
                }
            }
            i++
        }
        return issues
    }
}