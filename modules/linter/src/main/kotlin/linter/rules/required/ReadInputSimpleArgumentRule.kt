package linter.rules.required

import common.enums.FunctionEnum
import common.exception.InvalidPrintLnArgumentException
import common.token.FunctionToken
import common.token.VariableToken
import common.token.abs.TokenInterface
import linter.rules.abs.LinterRule

class ReadInputSimpleArgumentRule(
    private val enabled: Boolean = true,
) : LinterRule {
    override fun getName(): String = "read_input_simple_argument_rule"

    override fun match(tokens: List<TokenInterface>): List<Throwable> {
        val list = mutableListOf<Throwable>()
        if (!enabled) return list.toList()

        for (token in tokens) {
            if (token is FunctionToken && token.value == FunctionEnum.READ_INPUT) {
                // fixme -> asume caso perfecto
                if (tokens[tokens.indexOf(token) + 2] !is VariableToken) {
                    list.add(InvalidPrintLnArgumentException(token.getPosition()))
                }
            }
        }
        return list.toList()
    }
}
