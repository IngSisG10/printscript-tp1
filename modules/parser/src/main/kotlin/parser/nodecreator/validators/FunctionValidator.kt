package parser.nodecreator.validators

import common.exception.UnrecognizedLineException
import common.token.CloseParenthesisToken
import common.token.OpenParenthesisToken
import common.token.abs.TokenInterface
import parser.nodecreator.validators.abs.ParenthesisValidator
import parser.nodecreator.validators.abs.StructureValidator

// TODO: chequear el parentesis este bien hecho
class FunctionValidator :
    StructureValidator,
    ParenthesisValidator {
    override fun validate(line: List<TokenInterface>) {
        if (line.size > 2) {
            throw UnrecognizedLineException("Invalid function structure")
        }
        if (line[1] !is OpenParenthesisToken || line[line.size - 1] !is CloseParenthesisToken) {
            throw UnrecognizedLineException("Expected '(', got: ${line[1].name}")
        }
        if (!validateParenthesis(line)) {
            throw UnrecognizedLineException("Invalid function structure")
        }
    }
}
