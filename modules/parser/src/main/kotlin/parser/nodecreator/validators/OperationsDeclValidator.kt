package parser.nodecreator.validators

import common.enums.TypeEnum
import common.token.BooleanLiteralToken
import common.token.NumberLiteralToken
import common.token.StringLiteralToken
import common.token.abs.TokenInterface
import parser.nodecreator.validators.abs.StructureValidator

class OperationsDeclValidator(
    private val variableType: TypeEnum,
) : StructureValidator {
    override fun validate(line: List<TokenInterface>) {
        for (token in line) {
            when (variableType) {
                TypeEnum.NUMBER -> {
                    if (token is StringLiteralToken || token is BooleanLiteralToken) {
                        throw Exception("Type mismatch: expected NUMBER, found ${token.name}")
                    }
                }
                TypeEnum.STRING -> {
                    if (token is NumberLiteralToken || token is BooleanLiteralToken) {
                        throw Exception("Type mismatch: expected STRING, found ${token.name}")
                    }
                }
                TypeEnum.BOOLEAN -> {
                    if (token is NumberLiteralToken || token is StringLiteralToken) {
                        throw Exception("Type mismatch: expected BOOLEAN, found ${token.name}")
                    }
                }
                TypeEnum.ANY -> { /* No validation needed */ }
            }
        }
    }
}
