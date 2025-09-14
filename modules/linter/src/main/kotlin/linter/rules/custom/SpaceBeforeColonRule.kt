package linter.rules.custom

import common.exception.NoSpaceBeforeColonException
import common.token.TypeDeclaratorToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import linter.rules.abs.LinterRule

class SpaceBeforeColonRule : LinterRule {
    override fun getName(): String = "space_before_colon_rule"

    override fun match(tokens: List<TokenInterface>): List<Throwable> {
        val list = mutableListOf<Throwable>()
        for ((index, token) in tokens.withIndex()) {
            if (token is TypeDeclaratorToken) {
                if (tokens[index - 1] !is WhiteSpaceToken) {
                    list.add(NoSpaceBeforeColonException(token.getPosition())) // token or tokens[index - 1]?
                }
            }
        }
        return list.toList()
    }
}
