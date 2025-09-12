package linter.rules.required

import common.enums.FunctionEnum
import common.exception.InvalidPrintLnArgumentException
import common.token.FunctionToken
import common.token.VariableToken
import common.token.abs.TokenInterface
import linter.rules.abs.LinterRule

class PrintLnSimpleArgumentRule(
    private val enabled: Boolean = true,
) : LinterRule {
    override fun getName(): String = "println_simple_argument_rule"

    override fun match(tokens: List<TokenInterface>): Exception? {
        if (!enabled) return null

        for (token in tokens) {
            if (token is FunctionToken && token.value == FunctionEnum.PRINTLN) {
                if (tokens[tokens.indexOf(token) + 2] !is VariableToken) {
                    return InvalidPrintLnArgumentException(
                        "println must be called with identifier or literal at index ${token.position} row ${token.row}",
                    )
                }
            }
        }
        return null
    }
}
