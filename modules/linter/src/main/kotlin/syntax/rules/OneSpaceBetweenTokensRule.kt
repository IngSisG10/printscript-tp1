package syntax.rules

import data.LinterData
import exception.MoreThanOneSpaceAfterTokenException
import syntax.LinterRule
import token.abs.TokenInterface

class OneSpaceBetweenTokensRule : LinterRule {
    override fun match(tokens: List<TokenInterface>): Exception? {
        for (i in 0 until tokens.size - 1) {
            if (tokens[i] is token.WhiteSpaceToken && tokens[i + 1] is token.WhiteSpaceToken) {
                return MoreThanOneSpaceAfterTokenException("More than one space between tokens at index $i")
            }
        }
        return null
    }

    override fun matchWithData(tokens: List<TokenInterface>): List<LinterData> {
        val issues = mutableListOf<LinterData>()
        var i = 0
        while (i < tokens.size - 1) {
            if (tokens[i] is token.WhiteSpaceToken && tokens[i + 1] is token.WhiteSpaceToken) {
                issues.add(
                    LinterData(
                        exception = MoreThanOneSpaceAfterTokenException("More than one space between tokens at index $i"),
                        position = i,
                    ),
                )
                // Skip to the end of the whitespace sequence
                while (i < tokens.size - 1 && tokens[i + 1] is token.WhiteSpaceToken) {
                    i++
                }
            }
            i++
        }
        return issues
    }
}
