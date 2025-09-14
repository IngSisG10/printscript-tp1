package linter.rules.required

import common.exception.InvalidCamelCaseException
import common.token.VariableToken
import common.token.abs.TokenInterface
import linter.rules.abs.LinterRule

class CamelCaseRule : LinterRule {
    override fun getName(): String = "camel_case_rule"

    override fun match(tokens: List<TokenInterface>): List<Throwable> {
        val list = mutableListOf<Throwable>()
        for (token in tokens) {
            if (token is VariableToken) {
                val varName = token.value
                if (!varName.matches(Regex("\\b[a-z][a-z0-9]*(?:[A-Z0-9]+[a-z0-9]*)*\\b"))) {
                    list.add(InvalidCamelCaseException(token.getPosition()))
                }
            }
        }
        return list.toList()
    }
}
