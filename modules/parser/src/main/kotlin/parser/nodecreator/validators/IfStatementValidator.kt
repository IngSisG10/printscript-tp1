package parser.nodecreator.validators

import common.exception.UnrecognizedLineException
import common.token.BooleanLiteralToken
import common.token.CloseParenthesisToken
import common.token.IfToken
import common.token.OpenBraceToken
import common.token.OpenParenthesisToken
import common.token.abs.TokenInterface
import parser.nodecreator.validators.abs.StructureValidator

class IfStatementValidator : StructureValidator {
    override fun validate(line: List<TokenInterface>) {
        if (line[0] !is IfToken) {
            throw UnrecognizedLineException("Expected 'if' token at the beginning, got: ${line[0].name}")
        } else if (line[1] !is OpenParenthesisToken) {
            throw UnrecognizedLineException("Expected '(' token after 'if', got: ${line[1].name}")
        } else if (line[2] !is BooleanLiteralToken) {
            throw UnrecognizedLineException("Expected boolean literal inside parentheses, got: ${line[2].name}")
        } else if (line[3] !is CloseParenthesisToken) {
            throw UnrecognizedLineException("Expected ')' token after boolean literal, got: ${line[3].name}")
        } else if (line[4] !is OpenBraceToken) {
            throw UnrecognizedLineException("Expected '{' token to start the if block, got: ${line[4].name}")
        }
    }
}
