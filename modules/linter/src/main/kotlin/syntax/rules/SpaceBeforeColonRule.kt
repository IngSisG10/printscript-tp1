package syntax.rules

import data.LinterData
import exception.NoSpaceBeforeColonException
import syntax.LinterRule
import token.TypeDeclaratorToken
import token.WhiteSpaceToken
import token.abs.TokenInterface

class SpaceBeforeColonRule : LinterRule {
    override fun match(tokens: List<TokenInterface>): Exception? {
        for ((index, token) in tokens.withIndex()) {
            if (token is TypeDeclaratorToken) {
                if (tokens[index - 1] !is WhiteSpaceToken) {
                    throw NoSpaceBeforeColonException()
                }
            }
        }
        return null
    }

    override fun matchWithData(tokens: List<TokenInterface>): List<LinterData> {
        val list = mutableListOf<LinterData>()
        for ((index, token) in tokens.withIndex()) {
            if (token is TypeDeclaratorToken) {
                if (tokens[index - 1] !is WhiteSpaceToken) {
                    list.add(
                        LinterData(
                            exception = NoSpaceBeforeColonException(),
                            position = index,
                        ),
                    )
                }
            }
        }
        return list
    }
}
