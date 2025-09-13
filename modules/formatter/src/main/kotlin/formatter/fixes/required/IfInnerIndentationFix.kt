package formatter.fixes.required

import common.token.CloseBraceToken
import common.token.NewLineToken
import common.token.OpenBraceToken
import common.token.WhiteSpaceToken
import common.token.abs.TokenInterface
import formatter.fixes.abs.FormatterFix

class IfInnerIndentationFix(
    private val n: Int,
) : FormatterFix {
    override fun getName(): String = "if_white_space_token_fix"

    override fun fix(tokens: List<TokenInterface>): List<TokenInterface> = processBlock(tokens, 0)

    private fun processBlock(
        tokens: List<TokenInterface>,
        depth: Int,
    ): List<TokenInterface> {
        val result = mutableListOf<TokenInterface>()
        var i = 0

        while (i < tokens.size) {
            when (val token = tokens[i]) {
                is OpenBraceToken -> {
                    result.add(token)
                    val (blockTokens, consumed) = extractBlock(tokens, i + 1)
                    result.addAll(processBlock(blockTokens, depth + 1)) // recursive
                    result.add(tokens[i] as CloseBraceToken)
                    i = consumed
                }

                is NewLineToken -> {
                    result.add(token)
                    i++
                    i = fixIndentation(tokens, i, depth, result)
                }

                else -> {
                    result.add(token)
                    i++
                }
            }
        }

        return result
    }

    private fun fixIndentation(
        tokens: List<TokenInterface>,
        index: Int,
        depth: Int,
        result: MutableList<TokenInterface>,
    ): Int {
        var i = index
        var spaceCount = 0
        while (i < tokens.size && tokens[i] is WhiteSpaceToken) {
            spaceCount++
            i++
        }
        val expected = depth * n
        repeat(expected) { result.add(WhiteSpaceToken(tokens[i].row, tokens[i].position)) } // rebuild spaces
        return i
    }

    private fun extractBlock(
        tokens: List<TokenInterface>,
        start: Int,
    ): Pair<List<TokenInterface>, Int> {
        val blockTokens = mutableListOf<TokenInterface>()
        var depth = 1
        var i = start
        while (i < tokens.size && depth > 0) {
            if (tokens[i] is OpenBraceToken) {
                depth++
            } else if (tokens[i] is CloseBraceToken) {
                depth--
            }
            if (depth > 0) blockTokens.add(tokens[i])
            i++
        }
        return blockTokens to i // i -> is position after closing brace
    }
}
