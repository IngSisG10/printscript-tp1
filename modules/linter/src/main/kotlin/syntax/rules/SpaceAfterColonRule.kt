package syntax.rules

import data.LinterData
import exception.NoSpaceAfterColonException
import syntax.LinterRule
import token.TypeDeclaratorToken
import token.WhiteSpaceToken
import token.abs.TokenInterface

class SpaceAfterColonRule : LinterRule {
    override fun match(tokens: List<TokenInterface>): Exception? {
        for ((index, token) in tokens.withIndex()) {
            if (token is TypeDeclaratorToken) {
                if (tokens[index + 1] !is WhiteSpaceToken) {
                    throw NoSpaceAfterColonException()
                }
            }
        }
        return null
    }

    override fun matchWithData(tokens: List<TokenInterface>): List<LinterData> {
        val list = mutableListOf<LinterData>()
        for ((index, token) in tokens.withIndex()) {
            if (token is TypeDeclaratorToken) {
                if (tokens[index + 1] !is WhiteSpaceToken) {
                    list.add(
                        LinterData(
                            exception = NoSpaceAfterColonException(),
                            position = index,
                        ),
                    )
                }
            }
        }
        return list
    }

    override fun getName(): String = "space_after_colon_rule"
}
