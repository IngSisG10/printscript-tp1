package formatter.fixes.required

import common.token.OperationToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix
import kotlinx.serialization.json.JsonElement

class SpaceBeforeAndAfterOperatorFix : FormatterFix {
    override fun applies(fixesIWantToApply: Map<String, JsonElement>): Boolean =
        fixesIWantToApply.containsKey("mandatory-space-surrounding-operations") &&
            fixesIWantToApply["mandatory-space-surrounding-operations"]?.toString()?.toBoolean() == true

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in 1 until mutableTokens.size - 1) {
            val current = mutableTokens[i]

            if (current is OperationToken) {
                val previous = mutableTokens[i - 1]
                val next = mutableTokens[i + 1]

                var insertedBefore = false

                // Compruebo que el previo sea un espacio.
                if (previous !is WhiteSpaceToken) {
                    mutableTokens.add(i, WhiteSpaceToken(1, 2))
                    insertedBefore = true
                }

                val operationIndex = if (insertedBefore) i + 1 else i
                val nextToken = mutableTokens[operationIndex + 1]

                // Compruebo que el siguiente sea un espacio.
                if (nextToken !is WhiteSpaceToken) {
                    mutableTokens.add(operationIndex + 1, WhiteSpaceToken(1, 2))
                }
                return mutableTokens
            }
        }
        // No se ha hecho ninguna modificación, devuelvo los tokens originales.
        return mutableTokens
    }
}
