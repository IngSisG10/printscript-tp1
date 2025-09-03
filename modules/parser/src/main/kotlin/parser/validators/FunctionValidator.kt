package parser.validators

import common.exception.UnrecognizedLineException
import common.token.ParenthesisToken
import common.token.abs.TokenInterface
import parser.StructureValidator

class FunctionValidator : StructureValidator {
    override fun validate(line: List<common.token.abs.TokenInterface>) {
        if (line.size > 2) {
            throw common.exception.UnrecognizedLineException("Invalid function structure")
        }
        if (line[1] !is common.token.ParenthesisToken) {
            throw common.exception.UnrecognizedLineException("Expected '(', got: ${line[1].name}")
        }
    }
}
