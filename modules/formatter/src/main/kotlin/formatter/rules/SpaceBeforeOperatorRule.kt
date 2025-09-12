package formatter.rules

import common.data.FormatterData
import common.token.OperationToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import exception.NoSpaceBeforeOperatorException
import formatter.rules.abs.FormatterRule

class SpaceBeforeOperatorRule : FormatterRule {
    override fun getName(): String = "space_before_operator_rule"

    override fun matchWithData(tokens: List<TokenInterface>): List<FormatterData> {
        val issues = mutableListOf<FormatterData>()
        for (i in 0 until tokens.size + 1) {
            if (tokens[i] is OperationToken && tokens[i + 1] !is WhiteSpaceToken) {
                issues.add(
                    FormatterData(
                        exception = NoSpaceBeforeOperatorException("No space before operator at index $i"),
                        position = i,
                    ),
                )
            }
        }
        return issues
    }
}