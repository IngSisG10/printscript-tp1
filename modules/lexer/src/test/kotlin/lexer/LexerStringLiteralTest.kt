package lexer

import org.junit.jupiter.api.Test
import token.StringLiteralToken

class LexerStringLiteralTest {
    @Test
    fun testStringLiteral() {
        val tokens = Lexer().lex("\"hello world\"")
        assert(tokens.isNotEmpty()) { "Expected tokens to be generated for string literal" }
        assert(tokens.any { it is StringLiteralToken }) { "Expected String Literal Token" }
    }
}
