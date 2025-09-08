package common.converter

import common.enums.FunctionEnum
import common.enums.OperationEnum
import common.token.abs.TokenInterface

class Converter {
    fun convert(tokens: List<TokenInterface>): String {
        val builder = StringBuilder()
        for (token in tokens) {
            when (token.value) {
                is FunctionEnum -> builder.append(convertFunctionValue(token.value as FunctionEnum))
                is OperationEnum -> builder.append(convertOperationValue(token.value as OperationEnum))
            }
            builder.append(token.value)
        }
        return builder.toString()
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
        }
}
