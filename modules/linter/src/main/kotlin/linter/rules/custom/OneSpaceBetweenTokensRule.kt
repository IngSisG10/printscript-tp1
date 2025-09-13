package linter.rules.custom

import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import exception.MoreThanOneSpaceAfterTokenException
import linter.rules.abs.LinterRule

class OneSpaceBetweenTokensRule : LinterRule {
    override fun getName(): String = "one_space_between_tokens"

    override fun match(tokens: List<TokenInterface>): List<Throwable>? {
        val list = mutableListOf<Throwable>()
        for (i in 0 until tokens.size - 1) {
            if (tokens[i] is WhiteSpaceToken && tokens[i + 1] is WhiteSpaceToken) {
                list.add(MoreThanOneSpaceAfterTokenException("More than one space between tokens at index $i"))
            }
        }
        return list.toList()
    }
}
