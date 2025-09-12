package linter.rules.required

import common.exception.InvalidPascalCaseException
import common.token.VariableToken
import common.token.abs.TokenInterface
import linter.rules.abs.LinterRule

class PascalCaseRule : LinterRule {

    override fun getName(): String = "pascal_case_rule"

    override fun match(tokens: List<TokenInterface>): Exception? {
        for (token in tokens) {
            if (token is VariableToken) {
                val varName = token.value
                if (!varName.matches(Regex("\\b(?:[A-Z][a-z0-9]*)+\\b"))) {
                    return InvalidPascalCaseException(
                        "Invalid PascalCase identifier at row ${token.row}, index ${token.position}",
                    )
                }
            }
        }
        return null
    }
}
