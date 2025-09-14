package linter.rules.required

import common.enums.FunctionEnum
import common.exception.InvalidPrintLnArgumentException
import common.token.FunctionToken
import common.token.NumberLiteralToken
import common.token.StringLiteralToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import linter.rules.abs.LinterRule

class PrintLnSimpleArgumentRule(
    private val enabled: Boolean = true,
) : LinterRule {
    override fun getName(): String = "println_simple_argument_rule"

    override fun match(tokens: List<TokenInterface>): List<Throwable> {
        val list = mutableListOf<Throwable>()
        if (!enabled) return list.toList()

        for (i in tokens.indices) {
            val token = tokens[i]
            if (token is FunctionToken && token.value == FunctionEnum.PRINTLN) {
                var openParenIndex = -1
                for (j in i + 1 until tokens.size) {
                    if (tokens[j].value == "(") {
                        openParenIndex = j
                        break
                    }
                }

                if (openParenIndex == -1) {
                    list.add(InvalidPrintLnArgumentException(token.getPosition()))
                    continue
                }

                val argumentTokens = mutableListOf<TokenInterface>()
                var foundClosingParen = false

                for (k in openParenIndex + 1 until tokens.size) {
                    if (tokens[k].value == ")") {
                        foundClosingParen = true
                        break
                    }
                    argumentTokens.add(tokens[k])
                }

                if (!foundClosingParen) {
                    list.add(InvalidPrintLnArgumentException(token.getPosition()))
                    continue
                }

                if (!isValidArgument(argumentTokens)) {
                    list.add(InvalidPrintLnArgumentException(token.getPosition()))
                }
            }
        }
        return list.toList()
    }

    private fun isValidArgument(argumentTokens: List<TokenInterface>): Boolean {
        val relevantTokens =
            argumentTokens.filter {
                it.value
                    .toString()
                    .trim()
                    .isNotEmpty() &&
                    it !is WhiteSpaceToken
            }

        if (relevantTokens.size != 1) {
            return false
        }

        val singleToken = relevantTokens[0]

        return singleToken is StringLiteralToken || singleToken is VariableToken || singleToken is NumberLiteralToken
    }
}
