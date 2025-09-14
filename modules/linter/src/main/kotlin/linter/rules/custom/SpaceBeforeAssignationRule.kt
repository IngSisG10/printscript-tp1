package linter.rules.custom

import common.enums.OperationEnum
import common.token.OperationToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import exception.NoSpaceBeforeAssignationException
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive
import linter.rules.abs.LinterRule

// [a,' ',=,' ',5]
//  0  1  2  3  4
// a = 5

// a=5
// a =5 (prosigue la logica en el SpaceAfterAssignationRule)

class SpaceBeforeAssignationRule : LinterRule {
    override fun applies(rules: Map<String, JsonElement>): Boolean {
        for ((key, value) in rules) {
            if (key == "space-before-assignation-rule") {
                if (value.jsonPrimitive.boolean) {
                    return true
                }
            }
        }
        return false
    }

    override fun match(tokens: List<TokenInterface>): List<Throwable> {
        val list = mutableListOf<Throwable>()
        for ((index, token) in tokens.withIndex()) {
            if (token is OperationToken && token.value == OperationEnum.EQUAL) {
                if (tokens.getOrNull(index - 1) !is WhiteSpaceToken) {
                    list.add(NoSpaceBeforeAssignationException(token.getPosition())) // or tokens[index-1].getPosition()
                }
            }
        }
        return list.toList()
    }
}
