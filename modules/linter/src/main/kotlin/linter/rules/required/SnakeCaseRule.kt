package linter.rules.required

import common.exception.InvalidSnakeCaseException
import common.token.VariableToken
import common.token.abs.TokenInterface
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive
import linter.rules.abs.LinterRule

class SnakeCaseRule : LinterRule {
    override fun applies(rules: Map<String, JsonElement>): Boolean {
        for ((key, value) in rules) {
            if (key == "identifier_format") {
                if (value.jsonPrimitive.content == "snake case") {
                    return true
                }
            }
        }
        return false
    }

    override fun match(tokens: List<TokenInterface>): List<Throwable> {
        val list = mutableListOf<Throwable>()
        for (token in tokens) {
            if (token is VariableToken) {
                val varName = token.value
                if (!varName.matches(Regex("\\b[a-z0-9]+(?:_[a-z0-9]+)*\\b"))) {
                    list.add(InvalidSnakeCaseException(token.getPosition()))
                }
            }
        }
        return list.toList()
    }
}
