package formatter.rules

import common.data.FormatterData
import common.enums.OperationEnum
import common.token.OperationToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import exception.NoSpaceAfterAssignationException
import formatter.rules.abs.FormatterRule

class SpaceAfterAssignationRule : FormatterRule {
    override fun getName(): String = "space_after_assignation_rule"

    override fun matchWithData(tokens: List<TokenInterface>): List<FormatterData> {
        val list = mutableListOf<FormatterData>()
        for ((index, token) in tokens.withIndex()) {
            if (token is OperationToken && token.value == OperationEnum.EQUAL) {
                if (tokens.getOrNull(index + 1) !is WhiteSpaceToken) {
                    list.add(
                        FormatterData(
                            exception = NoSpaceAfterAssignationException(),
                            position = index,
                        ),
                    )
                }
            }
        }
        return list
    }
}