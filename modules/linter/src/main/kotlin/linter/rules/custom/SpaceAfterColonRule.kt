package linter.rules.custom

import common.exception.NoSpaceAfterColonException
import common.token.TypeDeclaratorToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import linter.rules.abs.LinterRule

class SpaceAfterColonRule : LinterRule {
    override fun getName(): String = "space_after_colon_rule"

    override fun match(tokens: List<TokenInterface>): List<Throwable> {
        val list = mutableListOf<Throwable>()
        for ((index, token) in tokens.withIndex()) {
            if (token is TypeDeclaratorToken) {
                if (tokens[index + 1] !is WhiteSpaceToken) {
                    list.add(NoSpaceAfterColonException(token.getPosition())) // token or tokens[index + 1]?
                }
            }
        }
        return list.toList()
    }
}
