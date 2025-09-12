package linter.rules.custom

import common.token.OperationToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import exception.NoSpaceBeforeOperatorException
import linter.rules.abs.LinterRule

class SpaceBeforeOperatorRule : LinterRule {
    override fun getName(): String = "space_before_operation"

    override fun match(tokens: List<TokenInterface>): Exception? {
        for (i in 0 until tokens.size + 1) {
            if (tokens[i] is OperationToken && tokens[i + 1] !is WhiteSpaceToken) {
                return NoSpaceBeforeOperatorException("No before after operator at index $i")
            }
        }
        return null
    }
}
