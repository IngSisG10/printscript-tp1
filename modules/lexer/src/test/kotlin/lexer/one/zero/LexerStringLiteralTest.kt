package lexer.one.zero

import common.token.StringLiteralToken
import lexer.Lexer
import org.junit.jupiter.api.Test

class LexerStringLiteralTest {
    @Test
    fun testStringLiteral() {
        val tokens = Lexer().lex("\"hello world\"")
        assert(tokens.isNotEmpty()) { "Expected tokens to be generated for string literal" }
        assert(tokens.all { it is StringLiteralToken }) { "Expected String Literal Token" }
    }
}
