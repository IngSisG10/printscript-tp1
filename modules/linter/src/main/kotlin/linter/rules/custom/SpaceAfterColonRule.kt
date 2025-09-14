package linter.rules.custom

import common.exception.NoSpaceAfterColonException
import common.token.TypeDeclaratorToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive
import linter.rules.abs.LinterRule

class SpaceAfterColonRule : LinterRule {
    override fun applies(rules: Map<String, JsonElement>): Boolean {
        for ((key, value) in rules) {
            if (key == "space-after-colon-rule") {
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
            if (token is TypeDeclaratorToken) {
                if (tokens[index + 1] !is WhiteSpaceToken) {
                    list.add(NoSpaceAfterColonException(token.getPosition())) // token or tokens[index + 1]?
                }
            }
        }
        return list.toList()
    }
}
