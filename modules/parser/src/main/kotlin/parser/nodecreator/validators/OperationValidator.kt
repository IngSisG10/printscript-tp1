package parser.nodecreator.validators

import common.exception.NoMatchingParenthesisException
import common.token.abs.TokenInterface
import parser.nodecreator.validators.abs.ParenthesisValidator
import parser.nodecreator.validators.abs.StructureValidator

class OperationValidator :
    StructureValidator,
    ParenthesisValidator {
    override fun validate(line: List<TokenInterface>) {
        if (!validateParenthesis(line)) {
            throw NoMatchingParenthesisException("Parenthesis must match")
        }
    }
}
