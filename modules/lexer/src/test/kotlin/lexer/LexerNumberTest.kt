package lexer

import common.token.NumberLiteralToken
import common.token.OperationToken
import common.token.WhiteSpaceToken
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class LexerNumberTest {
    @Test
    fun testSingleNumber() {
        val tokens = Lexer().lex("123")
        assertEquals(1, tokens.size)
        assertInstanceOf(NumberLiteralToken::class.java, tokens[0])
    }

    @Test
    fun testMultipleNumbersSeparatedBySpaces() {
        val tokens = Lexer().lex("0 1 2345")
        assertEquals(5, tokens.size)
        assertInstanceOf(NumberLiteralToken::class.java, tokens[0])
        assertInstanceOf(WhiteSpaceToken::class.java, tokens[1])
        assertInstanceOf(NumberLiteralToken::class.java, tokens[2])
        assertInstanceOf(WhiteSpaceToken::class.java, tokens[3])
        assertInstanceOf(NumberLiteralToken::class.java, tokens[4])
    }

    @Test
    fun testNumbersWithOperators() {
        val tokens = Lexer().lex("1+2-3*4/5")
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
        val tokens = Lexer().lex("-1 -2")
        // Expect: Op, N, Op, N
        assertEquals(5, tokens.size)
        assertInstanceOf(OperationToken::class.java, tokens[0])
        assertInstanceOf(NumberLiteralToken::class.java, tokens[1])
        assertInstanceOf(WhiteSpaceToken::class.java, tokens[2])
        assertInstanceOf(OperationToken::class.java, tokens[3])
        assertInstanceOf(NumberLiteralToken::class.java, tokens[4])
    }
}
