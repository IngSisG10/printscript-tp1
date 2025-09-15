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
            lexer.lex("if").all {
                it is IfToken
            },
        )
    }

    @Test
    fun testElseToken() {
        assert(
            lexer.lex("else").all {
                it is ElseToken
            },
        )
    }

    @Test
    fun testConstantDeclaratorToken() {
        assert(
            lexer.lex("const").all {
                it is ConstantDeclaratorToken
            },
        )
    }

    @Test
    fun testSomeIssue() {
        val tokens = lexer.lex("\r")
        assert(tokens.size == 1 && tokens[0].value == "\r" && tokens[0].name == "new_line")
    }
}
