package lexer

import org.junit.jupiter.api.Test

class LexerVariableTest {
    @Test
    fun generateVariableToken() {
        val lexer = Lexer()
        val tokens = lexer.lex("myVariable another_var OtherVariable var123")
        assert(tokens.size == 7) { "Expected 4 tokens, got ${tokens.size}" }
        assert(tokens.any { it is common.token.VariableToken }) { "All tokens should be VariableToken" }
    }
}
