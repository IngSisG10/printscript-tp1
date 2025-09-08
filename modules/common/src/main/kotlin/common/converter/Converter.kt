package common.converter

import common.enums.FunctionEnum
import common.enums.OperationEnum
import common.enums.TypeEnum
import common.token.abs.TokenInterface

class Converter {
    fun convert(tokens: List<TokenInterface>): String {
        val builder = StringBuilder()
        for (token in tokens) {
            when (token.value) {
                is FunctionEnum -> builder.append(convertFunctionValue(token.value as FunctionEnum))
                is OperationEnum -> builder.append(convertOperationValue(token.value as OperationEnum))
                is TypeEnum -> builder.append(convertTypeValue(token.value as TypeEnum))
                else -> builder.append(token.value)
            }
        }
        return builder.toString()
    }

    private fun convertTypeValue(value: TypeEnum): String =
        when (value) {
            TypeEnum.ANY -> "Any"
            TypeEnum.STRING -> "String"
            TypeEnum.NUMBER -> "Number"
            TypeEnum.BOOLEAN -> "Boolean"
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
