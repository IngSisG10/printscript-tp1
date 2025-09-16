package formatter.converter

import common.enums.FunctionEnum
import common.enums.OperationEnum
import common.enums.TypeEnum
import common.token.NumberLiteralToken
import common.token.StringLiteralToken
import common.token.abs.TokenInterface

class Converter {
    fun convert(tokens: List<TokenInterface>): String {
        val builder = StringBuilder()
        for (token in tokens) {
            when (token) {
                is StringLiteralToken -> {
                    builder.append("\"${token.value}\"")
                    continue
                }
                is NumberLiteralToken -> {
                    if (token.value.toFloat() % 1.0 == 0.0) {
                        builder.append(token.value.toInt())
                    } else {
                        builder.append(token.value)
                    }
                    continue
                }
            }
            when (token.value) {
                is FunctionEnum -> builder.append(convertFunctionValue(token.value as FunctionEnum))
                is OperationEnum -> builder.append(convertOperationValue(token.value as OperationEnum))
                is TypeEnum -> builder.append(convertTypeValue(token.value as TypeEnum))
                is Boolean -> builder.append(convertBooleanValue(token.value as Boolean))
                else -> builder.append(token.value)
            }
        }
        return builder.toString()
    }

    private fun convertBooleanValue(value: Boolean): String =
        if (value) {
            "true"
        } else {
            "false"
        }

    private fun convertTypeValue(value: TypeEnum): String =
        when (value) {
            TypeEnum.ANY -> "any"
            TypeEnum.STRING -> "string"
            TypeEnum.NUMBER -> "number"
            TypeEnum.BOOLEAN -> "boolean"
        }

    private fun convertOperationValue(value: OperationEnum): String =
        when (value) {
            OperationEnum.SUM -> "+"
            OperationEnum.DIVIDE -> "/"
            OperationEnum.EQUAL -> "="
            OperationEnum.MINUS -> "-"
            OperationEnum.MULTIPLY -> "*"
        }

    private fun convertFunctionValue(value: FunctionEnum): String =
        when (value) {
            FunctionEnum.PRINTLN -> "println"
            FunctionEnum.READ_INPUT -> "readInput"
            FunctionEnum.READ_ENV -> "readEnv"
        }
}
