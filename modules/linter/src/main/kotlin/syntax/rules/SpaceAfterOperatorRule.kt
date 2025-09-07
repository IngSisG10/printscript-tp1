package syntax.rules

import data.LinterData
import exception.NoSpaceAfterOperatorException
import syntax.LinterRule

class SpaceAfterOperatorRule : LinterRule {
    override fun match(tokens: List<token.abs.TokenInterface>): Exception? {
        for (i in 0 until tokens.size - 1) {
            if (tokens[i] is token.OperationToken && tokens[i + 1] !is token.WhiteSpaceToken) {
                return NoSpaceAfterOperatorException("No space after operator at index $i")
            }
        }
        return null
    }

    override fun matchWithData(tokens: List<token.abs.TokenInterface>): List<data.LinterData> {
        val issues = mutableListOf<LinterData>()
        for (i in 0 until tokens.size - 1) {
            if (tokens[i] is token.OperationToken && tokens[i + 1] !is token.WhiteSpaceToken) {
                issues.add(
                    LinterData(
                        exception = NoSpaceAfterOperatorException("No space after operator at index $i"),
                        position = i,
                    ),
                )
            }
        }
        return issues
    }
}
