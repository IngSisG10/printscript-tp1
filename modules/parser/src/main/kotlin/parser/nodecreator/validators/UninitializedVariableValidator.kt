package parser.nodecreator.validators

import common.exception.UnrecognizedLineException
import common.token.TypeDeclaratorToken
import common.token.TypeToken
import common.token.VariableToken
import common.token.abs.TokenInterface
import parser.nodecreator.validators.abs.StructureValidator

class UninitializedVariableValidator : StructureValidator {
    override fun validate(line: List<TokenInterface>) {
        if (line[1] !is VariableToken) {
            throw UnrecognizedLineException("Expected variable name, got: ${line[1].name}")
        } else if (line[2] !is TypeDeclaratorToken) {
            throw UnrecognizedLineException("Expected type declarator, got: ${line[2].name}")
        } else if (line[3] !is TypeToken) {
            throw UnrecognizedLineException("Expected type, got: ${line[3].name}")
        }
    }
}
