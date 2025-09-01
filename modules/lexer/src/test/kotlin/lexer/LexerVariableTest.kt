package lexer

import org.junit.jupiter.api.Test

class LexerVariableTest {
    @Test
    fun generateVariableToken() {
        val lexer = Lexer("myVariable another_var OtherVariable var123")
        val tokens = lexer.lex()
        assert(tokens.size == 4) { "Expected 4 tokens, got ${tokens.size}" }
        assert(tokens.all { it is token.VariableToken }) { "All tokens should be VariableToken" }
    }
}
