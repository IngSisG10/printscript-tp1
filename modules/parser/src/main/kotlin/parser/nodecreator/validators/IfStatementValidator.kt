package parser.nodecreator.validators

import common.exception.UnrecognizedLineException
import common.token.BooleanLiteralToken
import common.token.CloseParenthesisToken
import common.token.IfToken
import common.token.OpenBraceToken
import common.token.OpenParenthesisToken
import common.token.VariableToken
import common.token.abs.TokenInterface
import parser.nodecreator.validators.abs.StructureValidator
// el parser ignora los whitespace & newLine tokens
// estructura esperada:
// IfToken, OpenParenthesisToken, BooleanLiteralToken, CloseParenthesisToken, OpenBraceToken

class IfStatementValidator : StructureValidator {
    override fun validate(line: List<TokenInterface>) {
        if (line[0] !is IfToken) {
            throw UnrecognizedLineException("Expected 'if' token at the beginning, got: ${line[0].name}")
        } else if (line[1] !is OpenParenthesisToken) {
            throw UnrecognizedLineException("Expected '(' token after 'white_space' , got: ${line[1].name}")
        } else if (line[2] !is BooleanLiteralToken && line[2] !is VariableToken) {
            throw UnrecognizedLineException("Expected 'boolean_literal' or 'variable' token between parentheses, got: ${line[2].name}")
        } else if (line[3] !is CloseParenthesisToken) {
            throw UnrecognizedLineException("Expected ')' token after 'boolean_literal', got: ${line[3].name}")
        } else if (line[4] !is OpenBraceToken) {
            throw UnrecognizedLineException("Expected '{' token after 'white_space', got: ${line[4].name}")
        }
    }
}
