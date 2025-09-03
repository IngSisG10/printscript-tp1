package lexer.one.zero

import common.token.NewLineToken
import common.token.WhiteSpaceToken
import lexer.Lexer
import org.junit.jupiter.api.Test

class LexerVariableTest {
    @Test
    fun generateVariableToken() {
        val lexer = Lexer()
        val tokens =
            lexer.lex("myVariable another_var OtherVariable var123").filterNot {
                it is WhiteSpaceToken || it is NewLineToken
            }
        assert(tokens.size == 4) { "Expected 4 tokens, got ${tokens.size}" }
        assert(tokens.all { it is common.token.VariableToken }) { "All tokens should be VariableToken" }
    }
}
