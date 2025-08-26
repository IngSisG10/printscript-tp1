package lexer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import token.FunctionToken
import token.TypeToken
import token.VariableDeclaratorToken
import token.VariableToken

class LexerKeywordsTest {
    @Test
    fun testKeywordsInOneLine() {
        val tokens = Lexer("println let String Boolean Number Any").lex()
        assertEquals(6, tokens.size)

        assertInstanceOf(FunctionToken::class.java, tokens[0])
        assertInstanceOf(VariableDeclaratorToken::class.java, tokens[1])
        assertInstanceOf(TypeToken::class.java, tokens[2])
        assertInstanceOf(TypeToken::class.java, tokens[3])
        assertInstanceOf(TypeToken::class.java, tokens[4])
        assertInstanceOf(TypeToken::class.java, tokens[5])
    }

    @Test
    fun testEachKeywordIndividually() {
        val cases =
            listOf(
                "println" to FunctionToken::class.java,
                "let" to VariableDeclaratorToken::class.java,
                "String" to TypeToken::class.java,
                "Boolean" to TypeToken::class.java,
                "Number" to TypeToken::class.java,
                "Any" to TypeToken::class.java,
            )

        for ((code, expectedType) in cases) {
            val tokens = Lexer(code).lex()
            assertEquals(1, tokens.size, "Unexpected token count for input: $code")
            assertInstanceOf(expectedType, tokens.first(), "Unexpected token type for input: $code")
        }
    }

    @Test
    fun testKeywordsAreCaseSensitive() {
        val tokens = Lexer("Println LET STRING boolean number any").lex()
        assertEquals(6, tokens.size)
        tokens.forEach {
            assertInstanceOf(VariableToken::class.java, it)
        }
    }
}
