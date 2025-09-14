package linter.rules.custom

import common.token.OperationToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import exception.NoSpaceAfterOperatorException
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive
import linter.rules.abs.LinterRule

class SpaceAfterOperatorRule : LinterRule {
    override fun applies(rules: Map<String, JsonElement>): Boolean {
        for ((key, value) in rules) {
            if (key == "space-after-operator-rule") {
                if (value.jsonPrimitive.boolean) {
                    return true
                }
            }
        }
        return false
    }

    override fun match(tokens: List<TokenInterface>): List<Throwable> {
        val list = mutableListOf<Throwable>()
        for (i in 0 until tokens.size - 1) {
            if (tokens[i] is OperationToken && tokens[i + 1] !is WhiteSpaceToken) {
                list.add(NoSpaceAfterOperatorException(tokens[i].getPosition()))
            }
        }
        return list.toList()
    }
}
