package linter.rules.required

import common.exception.InvalidPascalCaseException
import common.token.VariableToken
import common.token.abs.TokenInterface
import linter.rules.abs.LinterRule

class PascalCaseRule : LinterRule {
    override fun getName(): String = "pascal_case_rule"

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
}
