package parser.validators

import common.enums.OperationEnum
import common.exception.UnrecognizedLineException
import common.token.OperationToken
import common.token.TypeDeclaratorToken
import common.token.TypeToken
import common.token.VariableToken
import common.token.abs.TokenInterface
import parser.StructureValidator

class DeclarationValidator : StructureValidator {
    override fun validate(line: List<common.token.abs.TokenInterface>) {
        if (line.size < 6) {
            throw common.exception.UnrecognizedLineException("Invalid declaration structure")
        }

        if (line[1] !is common.token.VariableToken) {
            throw common.exception.UnrecognizedLineException("Expected variable name, got: ${line[1].name}")
        } else if (line[2] !is common.token.TypeDeclaratorToken) {
            throw common.exception.UnrecognizedLineException("Expected type declarator, got: ${line[2].name}")
        } else if (line[3] !is common.token.TypeToken) {
            throw common.exception.UnrecognizedLineException("Expected type, got: ${line[3].name}")
        } else if (line[4] !is common.token.OperationToken ||
            (line[4] as common.token.OperationToken).value != common.enums.OperationEnum.EQUAL
        ) {
            throw common.exception.UnrecognizedLineException("Expected assignment operator '=', got: ${line[4].name}")
        }
    }
}
