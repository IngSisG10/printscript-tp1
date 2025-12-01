package linter.rules.required

import common.exception.InvalidPascalCaseException
import common.token.VariableToken
import common.token.abs.TokenInterface
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive
import linter.dto.LinterDto
import linter.rules.abs.LinterRule

class PascalCaseRule : LinterRule {
    override fun applies(rules: Map<String, JsonElement>): Boolean {
        for ((key, value) in rules) {
            if (key == "identifier_format") {
                if (value.jsonPrimitive.content == "pascal case") {
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
                if (!varName.matches(Regex("\\b(?:[A-Z][a-z0-9]*)+\\b"))) {
                    list.add(InvalidPascalCaseException(token.getPosition()))
                }
            }
        }
        return list.toList()
    }

    override fun getRuleNameAndValue(): LinterDto =
        LinterDto(
            name = "identifier_format",
            data =
                listOf(
                    linter.dto.DataItem(
                        value = "pascal case",
                        default = "pascal case",
                        type = "string",
                    ),
                ),
        )
}
