package lexer

import common.token.CloseParenthesisToken
import common.token.NewLineToken
import common.token.OpenParenthesisToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import org.junit.jupiter.api.Test

class LexerParenthesisTest {
    @Test
    fun createParenthesisWithContent() {
        val tokens =
            Lexer().lex(" ( Value ) ").filterNot {
                it is WhiteSpaceToken || it is NewLineToken
            }
        assert(
            tokens[0] is OpenParenthesisToken &&
                tokens[1] is VariableToken &&
                tokens[2] is CloseParenthesisToken,
        )
    }
}
