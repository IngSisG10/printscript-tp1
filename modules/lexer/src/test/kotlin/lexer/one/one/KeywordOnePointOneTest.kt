package lexer.one.one

import common.token.ConstantDeclaratorToken
import common.token.ElseToken
import common.token.IfToken
import lexer.util.LexerUtil.Companion.createLexer
import org.junit.jupiter.api.Test

class KeywordOnePointOneTest {
    private val lexer = createLexer("1.1")

    @Test
    fun testIfToken() {
        assert(
            lexer.lex("if".byteInputStream()).all {
                it is IfToken
            },
        )
    }

    @Test
    fun testElseToken() {
        assert(
            lexer.lex("else".byteInputStream()).all {
                it is ElseToken
            },
        )
    }

    @Test
    fun testConstantDeclaratorToken() {
        assert(
            lexer.lex("const".byteInputStream()).all {
                it is ConstantDeclaratorToken
            },
        )
    }
}
