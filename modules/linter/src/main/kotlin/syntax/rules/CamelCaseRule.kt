package syntax.rules

import data.LinterData
import exception.InvalidCamelCaseException
import syntax.LinterRule
import token.VariableToken
import token.abs.TokenInterface

class CamelCaseRule : LinterRule {
    override fun match(tokens: List<TokenInterface>): Exception? {
        for (token in tokens) {
            if (token is VariableToken) {
                val varName = token.value
                if (!varName.matches(Regex("\\b[a-z][a-z0-9]*(?:[A-Z0-9]+[a-z0-9]*)*\\b"))) {
                    return InvalidCamelCaseException("Invalid camelCase identifier at row ${token.row}, index ${token.position}")
                }
            }
        }
        return null
    }

    override fun matchWithData(tokens: List<TokenInterface>): List<LinterData> {
        val list = mutableListOf<LinterData>()
        for ((index, token) in tokens.withIndex()) {
            if (token is VariableToken) {
                val varName = token.value
                if (!varName.matches(Regex("\\b[a-z][a-z0-9]*(?:[A-Z0-9]+[a-z0-9]*)*\\b"))) {
                    list.add(
                        LinterData(
                            position = index,
                            exception =
                                InvalidCamelCaseException(
                                    "Invalid camelCase identifier at row ${token.row}, index ${token.position}",
                                ),
                        ),
                    )
                }
            }
        }
        return list
    }
}
