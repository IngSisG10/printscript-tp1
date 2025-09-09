package linter.syntax.rules

import common.data.LinterData
import common.token.OperationToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import exception.NoSpaceBeforeOperatorException
import linter.syntax.LinterRule

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

    override fun matchWithData(tokens: List<TokenInterface>): List<LinterData> {
        val issues = mutableListOf<LinterData>()
        for (i in 0 until tokens.size + 1) {
            if (tokens[i] is OperationToken && tokens[i + 1] !is WhiteSpaceToken) {
                issues.add(
                    LinterData(
                        exception = NoSpaceBeforeOperatorException("No space before operator at index $i"),
                        position = i,
                    ),
                )
            }
        }
        return issues
    }
}
