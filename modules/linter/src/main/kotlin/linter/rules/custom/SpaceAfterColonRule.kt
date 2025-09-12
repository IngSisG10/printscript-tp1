package linter.rules.custom

import common.exception.NoSpaceAfterColonException
import common.token.TypeDeclaratorToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import linter.rules.abs.LinterRule

class SpaceAfterColonRule : LinterRule {
    override fun match(tokens: List<TokenInterface>): Exception? {
        for ((index, token) in tokens.withIndex()) {
            if (token is TypeDeclaratorToken) {
                if (tokens[index + 1] !is WhiteSpaceToken) {
                    throw NoSpaceAfterColonException()
                }
            }
        }
        return null
    }

    override fun getName(): String = "space_after_colon_rule"
}
