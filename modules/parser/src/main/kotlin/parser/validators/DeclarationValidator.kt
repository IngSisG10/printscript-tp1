package parser.validators

import enums.OperationEnum
import exception.UnrecognizedLineException
import parser.StructureValidator
import token.OperationToken
import token.TypeDeclaratorToken
import token.TypeToken
import token.VariableToken
import token.abs.TokenInterface

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
        } else if (line[4] !is OperationToken || (line[4] as OperationToken).value != OperationEnum.EQUAL) {
            throw UnrecognizedLineException("Expected assignment operator '=', got: ${line[4].name}")
        }
    }
}
