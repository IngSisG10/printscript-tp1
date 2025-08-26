package lexer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import token.NumberLiteralToken
import token.OperationToken

class LexerNumberTest {
    @Test
    fun testSingleNumber() {
        val tokens = Lexer("123").lex()
        assertEquals(1, tokens.size)
        assertInstanceOf(NumberLiteralToken::class.java, tokens[0])
    }

    @Test
    fun testMultipleNumbersSeparatedBySpaces() {
        val tokens = Lexer("0 1 2345").lex()
        assertEquals(3, tokens.size)
        tokens.forEach { assertInstanceOf(NumberLiteralToken::class.java, it) }
    }

    @Test
    fun testNumbersWithOperators() {
        val tokens = Lexer("1+2-3*4/5").lex()
        // Expect: N, Op, N, Op, N, Op, N, Op, N  -> 9 tokens
        assertEquals(9, tokens.size)
        for (i in tokens.indices) {
            if (i % 2 == 0) {
                assertInstanceOf(NumberLiteralToken::class.java, tokens[i], "Expected number at index $i")
            } else {
                assertInstanceOf(OperationToken::class.java, tokens[i], "Expected operator at index $i")
            }
        }
    }

    @Test
    fun testNegativeNumbersAreMinusThenNumber() {
        val tokens = Lexer("-1 -2").lex()
        // Expect: Op, N, Op, N
        assertEquals(4, tokens.size)
        assertInstanceOf(OperationToken::class.java, tokens[0])
        assertInstanceOf(NumberLiteralToken::class.java, tokens[1])
        assertInstanceOf(OperationToken::class.java, tokens[2])
        assertInstanceOf(NumberLiteralToken::class.java, tokens[3])
    }
}
