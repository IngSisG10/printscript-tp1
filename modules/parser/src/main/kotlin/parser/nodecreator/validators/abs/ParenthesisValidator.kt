package parser.nodecreator.validators.abs

import common.token.CloseParenthesisToken
import common.token.OpenParenthesisToken
import common.token.abs.TokenInterface

interface ParenthesisValidator {
    fun validateParenthesis(line: List<TokenInterface>): Boolean {
        var openParenthesis = 0
        var closeParenthesis = 0
        for (token in line) {
            when (token) {
                is OpenParenthesisToken -> openParenthesis++
                is CloseParenthesisToken -> closeParenthesis++
            }
        }
        return openParenthesis == closeParenthesis
    }
}
