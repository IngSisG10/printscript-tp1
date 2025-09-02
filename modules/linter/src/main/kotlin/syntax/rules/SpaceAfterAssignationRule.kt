package syntax.rules

import data.LinterData
import enums.OperationEnum
import exception.NoSpaceAfterAssignationException
import syntax.LinterRule
import token.OperationToken
import token.abs.TokenInterface

// [a,' ',=,' ',5]
//  0  1  2  3  4
// a = 5

// a =5
// a = 5

class SpaceAfterAssignationRule : LinterRule {
    override fun match(tokens: List<TokenInterface>): Exception? {
        for ((index, token) in tokens.withIndex()) {
            if (token is OperationToken && token.value == OperationEnum.EQUAL) {
                // fixme analizar bien tema index+1 para el caso particular de Assignation
                if (tokens.getOrNull(index + 1) !is token.WhiteSpaceToken) {
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
                // fixme: analizar bien tema index+1 para el caso particular de Assignation
                if (tokens.getOrNull(index + 1) !is token.WhiteSpaceToken) {
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
