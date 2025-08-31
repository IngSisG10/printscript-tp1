package parser

import lexer.Lexer
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

// fixme
class ParameterizedParserTest {
    @ParameterizedTest
    @ValueSource(
        strings = [
            "let a: Number = 5;",
            "let b: String = \"hello\";",
            "let c: Boolean = true;",
            "let d: Number = 1 + 2;",
            "let e: Number = (3 * 4) - 5;",
        ],
    )
    fun `test valid declarations`(code: String) {
        val lexer = Lexer(code)
        val tokens = lexer.lex()
        val parser = Parser(tokens)

        assertDoesNotThrow {
            val ast = parser.parse()
            assertNotNull(ast)
            assertEquals(1, ast.size)
        }
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "let a = 5;", // Missing type
            "let : Number = 5;", // Missing identifier
            "let a Number = 5;", // Missing colon
            "a = ;", // Missing value
            "println(;", // Unclosed parenthesis
        ],
    )
    fun `test invalid syntax should throw exception`(code: String) {
        val lexer = Lexer(code)
        val tokens = lexer.lex()
        val parser = Parser(tokens)

        assertThrows<Exception> {
            parser.parse()
        }
    }
}
