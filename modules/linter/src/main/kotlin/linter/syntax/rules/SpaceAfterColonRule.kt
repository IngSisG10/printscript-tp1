package linter.syntax.rules

import common.data.LinterData
import common.exception.NoSpaceAfterColonException
import common.token.TypeDeclaratorToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import linter.syntax.LinterRule

class SpaceAfterColonRule : LinterRule {
    override fun match(tokens: List<common.token.abs.TokenInterface>): Exception? {
        for ((index, token) in tokens.withIndex()) {
            if (token is common.token.TypeDeclaratorToken) {
                if (tokens[index + 1] !is common.token.WhiteSpaceToken) {
                    throw common.exception.NoSpaceAfterColonException()
                }
            }
        }
        return null
    }

    override fun matchWithData(tokens: List<common.token.abs.TokenInterface>): List<common.data.LinterData> {
        val list = mutableListOf<common.data.LinterData>()
        for ((index, token) in tokens.withIndex()) {
            if (token is common.token.TypeDeclaratorToken) {
                if (tokens[index + 1] !is common.token.WhiteSpaceToken) {
                    list.add(
                        common.data.LinterData(
                            exception = common.exception.NoSpaceAfterColonException(),
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
