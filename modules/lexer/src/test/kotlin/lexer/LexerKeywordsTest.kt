package lexer

import common.token.FunctionToken
import common.token.TypeToken
import common.token.VariableDeclaratorToken
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class LexerKeywordsTest {
    @Test
    fun testKeywordsInOneLine() {
        val tokens = Lexer().lex("println let String Boolean Number Any")
        assertEquals(11, tokens.size)
    }

    @Test
    fun testEachKeywordIndividually() {
        val cases =
            listOf(
                "println" to common.token.FunctionToken::class.java,
                "let" to common.token.VariableDeclaratorToken::class.java,
                "String" to common.token.TypeToken::class.java,
                "Number" to common.token.TypeToken::class.java,
                "Any" to common.token.TypeToken::class.java,
            )

        for ((code, expectedType) in cases) {
            val tokens = Lexer().lex(code)
            assertEquals(1, tokens.size, "Unexpected token count for input: $code")
            assertInstanceOf(expectedType, tokens.first(), "Unexpected token type for input: $code")
        }
    }
}
