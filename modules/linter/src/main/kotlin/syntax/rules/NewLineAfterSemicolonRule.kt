package syntax.rules

import data.LinterData
import syntax.LinterRule
import token.EndSentenceToken
import token.abs.TokenInterface

class NewLineAfterSemicolonRule : LinterRule {
    override fun match(tokens: List<TokenInterface>): Exception? {
        for ((index, token) in tokens.withIndex()) {
            if (token is EndSentenceToken) {
                val next = tokens.getOrNull(index + 1)

                // fixme, esta logica es un poco rara
//                if (next !is NewLineToken) {
//                    return MissingNewLineAfterSemicolonException()
//                }
            }
        }
        return null
    }

    override fun matchWithData(tokens: List<TokenInterface>): List<LinterData> {
        val list = mutableListOf<LinterData>()
        for ((index, token) in tokens.withIndex()) {
            if (token is EndSentenceToken) {
                val next = tokens.getOrNull(index + 1)
                // fixme, esta logica es un poco rara
//                if (next !is NewLineToken) {
//                    list.add(
//                        LinterData(
//                            exception = MissingNewLineAfterSemicolonException(),
//                            position = index
//                        )
//                    )
//                }
            }
        }
        return list
    }
}
