package parser.validators

import exception.UnrecognizedLineException
import parser.StructureValidator
import token.ParenthesisToken
import token.abs.TokenInterface

class FunctionValidator : StructureValidator {
    override fun validate(line: List<TokenInterface>) {
        if (line.size > 2) {
            throw UnrecognizedLineException("Invalid function structure")
        }
        if (line[1] !is ParenthesisToken) {
            throw UnrecognizedLineException("Expected '(', got: ${line[1].name}")
        }
    }
}
