package parser.validators

import common.exception.UnrecognizedLineException
import common.token.OperationToken
import common.token.TypeDeclaratorToken
import common.token.TypeToken
import common.token.VariableToken
import common.token.abs.TokenInterface
import parser.StructureValidator

class DeclarationValidator : StructureValidator {
    override fun validate(line: List<TokenInterface>) {
        if (line.size < 6) {
            throw UnrecognizedLineException("Invalid declaration structure")
        }

        if (line[1] !is VariableToken) {
            throw UnrecognizedLineException("Expected variable name, got: ${line[1].name}")
        } else if (line[2] !is TypeDeclaratorToken) {
            throw UnrecognizedLineException("Expected type declarator, got: ${line[2].name}")
        } else if (line[3] !is TypeToken) {
            throw UnrecognizedLineException("Expected type, got: ${line[3].name}")
        } else if (line[4] !is OperationToken ||
            (line[4] as OperationToken).value != common.enums.OperationEnum.EQUAL
        ) {
            throw UnrecognizedLineException("Expected assignment operator '=', got: ${line[4].name}")
        }
    }
}
