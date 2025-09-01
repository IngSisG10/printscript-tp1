package lexer

import org.junit.jupiter.api.Test

class LexerVariableTest {
    @Test
    fun generateVariableToken() {
        val lexer = Lexer("myVariable another_var OtherVariable var123")
        val tokens = lexer.lex()
        assert(tokens.size == 7) { "Expected 4 tokens, got ${tokens.size}" }
        assert(tokens.any { it is token.VariableToken }) { "All tokens should be VariableToken" }
    }
}
