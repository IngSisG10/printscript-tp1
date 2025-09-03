package linter.syntax.rules

import data.LinterData
import exception.InvalidPascalCaseException
import linter.syntax.LinterRule
import token.VariableToken
import token.abs.TokenInterface

class PascalCaseRule : LinterRule {
    override fun match(tokens: List<TokenInterface>): Exception? {
        for (token in tokens) {
            if (token is VariableToken) {
                val varName = token.value
                if (!varName.matches(Regex("\\b(?:[A-Z][a-z0-9]*)+\\b"))) {
                    return InvalidPascalCaseException("Invalid PascalCase identifier at row ${token.row}, index ${token.position}")
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
                if (!varName.matches(Regex("\\b(?:[A-Z][a-z0-9]*)+\\b"))) {
                    list.add(
                        LinterData(
                            position = index,
                            exception =
                                InvalidPascalCaseException(
                                    "Invalid PascalCase identifier at row ${token.row}, index ${token.position}",
                                ),
                        ),
                    )
                }
            }
        }
        return list
    }

    override fun getName(): String = "pascal_case_rule"
}
