package fixes

import FormatterFix
import data.LinterData
import exception.NoSpaceAfterOperatorException
import exception.NoSpaceBeforeOperatorException
import token.abs.TokenInterface

class SpaceBeforeAndAfterOperatorFix : FormatterFix {
    override fun canFix(issue: LinterData): Boolean =
        issue.exception is NoSpaceBeforeOperatorException ||
            issue.exception is NoSpaceAfterOperatorException

    override fun fix(
        issue: LinterData,
        tokens: List<TokenInterface>,
    ): List<TokenInterface> {
        val mutableTokens = tokens.toMutableList()

        for (i in 1 until mutableTokens.size - 1) {
            val current = mutableTokens[i]

            if (current is token.OperationToken) {
                val previous = mutableTokens[i - 1]
                val next = mutableTokens[i + 1]

                var insertedBefore = false

                // Compruebo que el previo sea un espacio.
                if (previous !is token.WhiteSpaceToken) {
                    mutableTokens.add(i, token.WhiteSpaceToken(1, 2))
                    insertedBefore = true
                }

                val operationIndex = if (insertedBefore) i + 1 else i
                val nextToken = mutableTokens[operationIndex + 1]

                // Compruebo que el siguiente sea un espacio.
                if (nextToken !is token.WhiteSpaceToken) {
                    mutableTokens.add(operationIndex + 1, token.WhiteSpaceToken(1, 2))
                }
                return mutableTokens
            }
        }
        return mutableTokens
    }
}
