package linter.syntax.rules

import common.data.LinterData
import common.exception.InvalidCamelCaseException
import common.token.VariableToken
import common.token.abs.TokenInterface
import linter.syntax.LinterRule

class CamelCaseRule : LinterRule {
    override fun match(tokens: List<common.token.abs.TokenInterface>): Exception? {
        for (token in tokens) {
            if (token is common.token.VariableToken) {
                val varName = token.value
                if (!varName.matches(Regex("\\b[a-z][a-z0-9]*(?:[A-Z0-9]+[a-z0-9]*)*\\b"))) {
                    return common.exception.InvalidCamelCaseException(
                        "Invalid camelCase identifier at row ${token.row}, index ${token.position}",
                    )
                }
            }
        }
        return null
    }

    override fun matchWithData(tokens: List<common.token.abs.TokenInterface>): List<common.data.LinterData> {
        val list = mutableListOf<common.data.LinterData>()
        for ((index, token) in tokens.withIndex()) {
            if (token is common.token.VariableToken) {
                val varName = token.value
                if (!varName.matches(Regex("\\b[a-z][a-z0-9]*(?:[A-Z0-9]+[a-z0-9]*)*\\b"))) {
                    list.add(
                        common.data.LinterData(
                            position = index,
                            exception =
                                common.exception.InvalidCamelCaseException(
                                    "Invalid camelCase identifier at row ${token.row}, index ${token.position}",
                                ),
                        ),
                    )
                }
            }
        }
        return list
    }

    override fun getName(): String = "camel_case_rule"
}
