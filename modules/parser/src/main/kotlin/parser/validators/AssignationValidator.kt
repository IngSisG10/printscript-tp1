package parser.validators

import common.exception.UnrecognizedLineException
import common.token.OperationToken
import common.token.abs.TokenInterface
import parser.StructureValidator

class AssignationValidator : StructureValidator {
    override fun validate(line: List<TokenInterface>) {
        if (line.size < 3) {
            throw UnrecognizedLineException("Invalid assignment structure")
        }
        if (line[1] !is OperationToken && (line[1] as OperationToken).value != common.enums.OperationEnum.EQUAL) {
            throw UnrecognizedLineException("Expected assignment operator, got: ${line[1].name}")
        }
    }
}
