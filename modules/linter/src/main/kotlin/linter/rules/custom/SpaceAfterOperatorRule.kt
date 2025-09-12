package linter.rules.custom

import common.token.OperationToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import exception.NoSpaceAfterOperatorException
import linter.rules.abs.LinterRule

class SpaceAfterOperatorRule : LinterRule {
    override fun getName(): String = "space_after_operation"

    override fun match(tokens: List<TokenInterface>): Exception? {
        for (i in 0 until tokens.size - 1) {
            if (tokens[i] is OperationToken && tokens[i + 1] !is WhiteSpaceToken) {
                return NoSpaceAfterOperatorException("No space after operator at index $i")
            }
        }
        return null
    }
}
