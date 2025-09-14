package linter.rules.required

import common.enums.FunctionEnum
import common.exception.InvalidPrintLnArgumentException
import common.token.BooleanLiteralToken
import common.token.FunctionToken
import common.token.NumberLiteralToken
import common.token.StringLiteralToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive
import linter.rules.abs.LinterRule

class ReadInputSimpleArgumentRule(
    private val enabled: Boolean = true,
) : LinterRule {
    override fun applies(rules: Map<String, JsonElement>): Boolean {
        for ((key, value) in rules) {
            if (key == "mandatory-variable-or-literal-in-readInput") {
                if (value.jsonPrimitive.boolean) {
                    return true
                }
            }
        }
        return false
    }

    override fun match(tokens: List<TokenInterface>): List<Throwable> {
        val list = mutableListOf<Throwable>()
        if (!enabled) return list.toList()

        for (i in tokens.indices) {
            val token = tokens[i]

            val isReadInputFunction =
                when {
                    token is FunctionToken && token.value == FunctionEnum.READ_INPUT -> true
                    token is VariableToken && token.value.toString() == "readInput" -> true
                    else -> false
                }

            if (isReadInputFunction) {
                var openParenIndex = -1
                for (j in i + 1 until tokens.size) {
                    if (tokens[j].value.toString() == "(") {
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
                    if (tokens[k].value.toString() == ")") {
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
            argumentTokens.filter { token ->
                val value = token.value.toString().trim()
                val isNotEmpty = value.isNotEmpty()
                val isNotWhitespace = token !is WhiteSpaceToken
                val isNotComma = value != ","
                val isNotSemicolon = value != ";"

                isNotEmpty && isNotWhitespace && isNotComma && isNotSemicolon
            }

        if (relevantTokens.isEmpty()) {
            return false
        }

        if (relevantTokens.size > 1) {
            return false
        }

        val singleToken = relevantTokens[0]
        return when (singleToken) {
            is StringLiteralToken -> true // "Enter value"
            is NumberLiteralToken -> true // 42
            is VariableToken -> true // promptMessage
            is BooleanLiteralToken -> true // true/false
            else -> false // Operators, function calls, etc.
        }
    }
}
