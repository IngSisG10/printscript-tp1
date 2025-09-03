package linter.syntax.rules

import data.LinterData
import exception.InvalidSnakeCaseException
import linter.syntax.LinterRule
import token.VariableToken
import token.abs.TokenInterface

class SnakeCaseRule : LinterRule {
    override fun match(tokens: List<TokenInterface>): Exception? {
        for (token in tokens) {
            if (token is VariableToken) {
                val varName = token.value
                if (!varName.matches(Regex("\\b[a-z0-9]+(?:_[a-z0-9]+)*\\b"))) {
                    return InvalidSnakeCaseException("Invalid snake_case identifier at row ${token.row}, index ${token.position}")
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
                if (!varName.matches(Regex("\\b[a-z0-9]+(?:_[a-z0-9]+)*\\b"))) {
                    list.add(
                        LinterData(
                            position = index,
                            exception =
                                InvalidSnakeCaseException(
                                    "Invalid snake_case identifier at row ${token.row}, index ${token.position}",
                                ),
                        ),
                    )
                }
            }
        }
        return list
    }

    override fun getName(): String = "snake_case_rule"
}
