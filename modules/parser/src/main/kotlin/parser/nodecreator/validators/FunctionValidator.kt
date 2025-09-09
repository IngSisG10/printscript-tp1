package parser.nodecreator.validators

import common.exception.UnrecognizedLineException
import common.token.abs.TokenInterface
import parser.nodecreator.validators.abs.StructureValidator

// TODO: chequear el parentesis este bien hecho
class FunctionValidator : StructureValidator {
    override fun validate(line: List<TokenInterface>) {
        if (line.size > 2) {
            throw UnrecognizedLineException("Invalid function structure")
        }
        if (line[1] !is common.token.ParenthesisToken) {
            throw UnrecognizedLineException("Expected '(', got: ${line[1].name}")
        }
    }
}
