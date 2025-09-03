package parser.validators

import common.enums.OperationEnum
import common.exception.UnrecognizedLineException
import common.token.OperationToken
import common.token.abs.TokenInterface
import parser.StructureValidator

class AssignationValidator : StructureValidator {
    override fun validate(line: List<common.token.abs.TokenInterface>) {
        if (line.size < 3) {
            throw common.exception.UnrecognizedLineException("Invalid assignment structure")
        }
        if (line[1] !is common.token.OperationToken && (line[1] as common.token.OperationToken).value != common.enums.OperationEnum.EQUAL) {
            throw common.exception.UnrecognizedLineException("Expected assignment operator, got: ${line[1].name}")
        }
    }
}
