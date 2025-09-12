package linter.rules.custom

import common.exception.InvalidSnakeCaseException
import common.token.VariableToken
import common.token.abs.TokenInterface
import linter.rules.abs.LinterRule

class SnakeCaseRule : LinterRule {

    override fun getName(): String = "snake_case_rule"

    override fun match(tokens: List<TokenInterface>): Exception? {
        for (token in tokens) {
            if (token is VariableToken) {
                val varName = token.value
                if (!varName.matches(Regex("\\b[a-z0-9]+(?:_[a-z0-9]+)*\\b"))) {
                    return InvalidSnakeCaseException(
                        "Invalid snake_case identifier at row ${token.row}, index ${token.position}",
                    )
                }
            }
        }
        return null
    }
}
