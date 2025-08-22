package lexer

import exception.NoMatchingQuotationMarksException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import token.StringLiteralToken

class LexerStringLiteralTest {
    @Test
    fun testStringLiteral() {
        val tokens = Lexer("\"hello world\"").lex()
        assert(tokens.isNotEmpty()) { "Expected tokens to be generated for string literal" }
        assert(tokens.any { it is StringLiteralToken }) { "Expected String Literal Token" }
    }

    @Test
    fun testOpenStringLiteral() {
        assertThrows(NoMatchingQuotationMarksException::class.java) {
            (Lexer("\"hello world").lex())
        }
    }
}
