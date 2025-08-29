package parser.validators

import enums.OperationEnum
import exception.UnrecognizedLineException
import parser.StructureValidator
import token.OperationToken
import token.abs.TokenInterface

class AssignationValidator : StructureValidator {
    override fun validate(line: List<TokenInterface>) {
        if (line.size < 3) {
            throw UnrecognizedLineException("Invalid assignment structure")
        }
        if (line[1] !is OperationToken && (line[1] as OperationToken).value != OperationEnum.EQUAL) {
            throw UnrecognizedLineException("Expected assignment operator, got: ${line[1].name}")
        }
    }
}
