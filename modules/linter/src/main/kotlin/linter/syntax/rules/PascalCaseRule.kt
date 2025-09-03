package linter.syntax.rules

import common.data.LinterData
import common.exception.InvalidPascalCaseException
import common.token.VariableToken
import common.token.abs.TokenInterface
import linter.syntax.LinterRule

class PascalCaseRule : LinterRule {
    override fun match(tokens: List<common.token.abs.TokenInterface>): Exception? {
        for (token in tokens) {
            if (token is common.token.VariableToken) {
                val varName = token.value
                if (!varName.matches(Regex("\\b(?:[A-Z][a-z0-9]*)+\\b"))) {
                    return common.exception.InvalidPascalCaseException(
                        "Invalid PascalCase identifier at row ${token.row}, index ${token.position}",
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
                if (!varName.matches(Regex("\\b(?:[A-Z][a-z0-9]*)+\\b"))) {
                    list.add(
                        common.data.LinterData(
                            position = index,
                            exception =
                                common.exception.InvalidPascalCaseException(
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
