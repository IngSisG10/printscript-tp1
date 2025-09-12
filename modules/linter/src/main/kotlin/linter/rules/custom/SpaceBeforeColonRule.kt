package linter.rules.custom

import common.exception.NoSpaceBeforeColonException
import common.token.TypeDeclaratorToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import linter.rules.abs.LinterRule

class SpaceBeforeColonRule : LinterRule {
    override fun match(tokens: List<TokenInterface>): Exception? {
        for ((index, token) in tokens.withIndex()) {
            if (token is TypeDeclaratorToken) {
                if (tokens[index - 1] !is WhiteSpaceToken) {
                    throw NoSpaceBeforeColonException()
                }
            }
        }
        return null
    }

    override fun getName(): String = "space_before_colon_rule"
}
