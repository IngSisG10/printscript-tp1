package linter.syntax.rules

import common.data.LinterData
import common.enums.OperationEnum
import common.token.OperationToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import exception.NoSpaceAfterAssignationException
import linter.syntax.LinterRule

// [a,' ',=,' ',5]
//  0  1  2  3  4
// a = 5

// a =5
// a = 5

class SpaceAfterAssignationRule : LinterRule {
    override fun getName(): String = "space_after_assignation"

    override fun match(tokens: List<TokenInterface>): Exception? {
        for ((index, token) in tokens.withIndex()) {
            if (token is OperationToken && token.value == OperationEnum.EQUAL) {
                if (tokens.getOrNull(index + 1) !is WhiteSpaceToken) {
                    throw NoSpaceAfterAssignationException()
                }
            }
        }
        return null
    }

    override fun matchWithData(tokens: List<TokenInterface>): List<LinterData> {
        val list = mutableListOf<LinterData>()
        for ((index, token) in tokens.withIndex()) {
            if (token is OperationToken && token.value == OperationEnum.EQUAL) {
                if (tokens.getOrNull(index + 1) !is WhiteSpaceToken) {
                    list.add(
                        LinterData(
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
