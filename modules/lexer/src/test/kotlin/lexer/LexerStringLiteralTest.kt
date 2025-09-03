package lexer

import common.token.StringLiteralToken
import org.junit.jupiter.api.Test

class LexerStringLiteralTest {
    @Test
    fun testStringLiteral() {
        val tokens = Lexer().lex("\"hello world\"")
        assert(tokens.isNotEmpty()) { "Expected tokens to be generated for string literal" }
        assert(tokens.any { it is common.token.StringLiteralToken }) { "Expected String Literal Token" }
    }
}
