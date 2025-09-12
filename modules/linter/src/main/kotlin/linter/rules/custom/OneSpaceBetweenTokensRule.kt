package linter.syntax.rules

import common.data.FormatterData
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import exception.MoreThanOneSpaceAfterTokenException
import linter.syntax.LinterRule

class OneSpaceBetweenTokensRule : LinterRule {
    override fun getName(): String = "one_space_between_tokens"

    override fun match(tokens: List<TokenInterface>): Exception? {
        for (i in 0 until tokens.size - 1) {
            if (tokens[i] is WhiteSpaceToken && tokens[i + 1] is WhiteSpaceToken) {
                return MoreThanOneSpaceAfterTokenException("More than one space between tokens at index $i")
            }
        }
        return null
    }
}
