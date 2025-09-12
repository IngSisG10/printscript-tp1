package formatter.rules

import common.data.FormatterData
import common.exception.NoSpaceAfterColonException
import common.token.TypeDeclaratorToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.rules.abs.FormatterRule

// todo: generalizarlo para manejar after y before
// SpaceColonRule (maneja tanto after como before)

class SpaceAfterColonRule : FormatterRule {
    override fun getName(): String = "space_after_colon_rule"

    override fun matchWithData(tokens: List<TokenInterface>): List<FormatterData> {
        val list = mutableListOf<FormatterData>()
        for ((index, token) in tokens.withIndex()) {
            if (token is TypeDeclaratorToken) {
                if (tokens[index + 1] !is WhiteSpaceToken) {
                    list.add(
                        FormatterData(
                            exception = NoSpaceAfterColonException(),
                            position = index,
                        ),
                    )
                }
            }
        }
        return list
    }
}
