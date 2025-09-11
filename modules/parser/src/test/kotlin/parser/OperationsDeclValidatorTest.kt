package parser

import lexer.Lexer
import lexer.token.TokenRule
import lexer.token.rules.IdentifierRule
import lexer.token.rules.KeywordOnePointOneRule
import lexer.token.rules.KeywordRule
import lexer.token.rules.NumberLiteralRule
import lexer.token.rules.ParenthesisRule
import lexer.token.rules.SingleCharOnePointOneRule
import lexer.token.rules.SingleCharRule
import lexer.token.rules.StringLiteralRule
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Test

class OperationsDeclValidatorTest {
    private val versionOnePointOne =
        listOf<TokenRule>(
            StringLiteralRule(),
            NumberLiteralRule(),
            KeywordRule(),
            KeywordOnePointOneRule(),
            ParenthesisRule(),
            SingleCharRule(),
            SingleCharOnePointOneRule(),
            IdentifierRule(),
        )

    private fun parseCode(code: String): List<Any> {
        val lexer = Lexer(versionOnePointOne)
        val tokens = lexer.lex(code)
        val parser = Parser()
        return parser.parse(tokens)
    }

    @Test
    fun `test NUMBER type with invalid decl should throw an exception`() {
        val code =
            """
            let a: Number = true + "Hello";
            """.trimIndent()

        assertThrows(Exception::class.java) { parseCode(code) }
    }

    @Test
    fun `test NUMBER type with number literal should not throw exception`() {
        val code =
            """
            let a: number = 5;
            """.trimIndent()

        assertDoesNotThrow { parseCode(code) }
    }

    @Test
    fun `test NUMBER type with boolean literal should throw exception`() {
        val code =
            """
            let a: number = true;
            """.trimIndent()

        assertThrows(Exception::class.java) { parseCode(code) }
    }

    @Test
    fun `test STRING type with valid string literal should not throw`() {
        val code =
            """
            let a: string = "Hello world";
            """.trimIndent()

        assertDoesNotThrow { parseCode(code) }
    }

    @Test
    fun `test STRING type with number literal should throw exception`() {
        val code =
            """
            let a: string = 5;
            """.trimIndent()

        assertThrows(Exception::class.java) { parseCode(code) }
    }

    @Test
    fun `test STRING type with boolean literal should throw exception`() {
        val code =
            """
            let a: string = true;
            """.trimIndent()

        assertThrows(Exception::class.java) { parseCode(code) }
    }

    @Test
    fun `test BOOLEAN type with valid boolean literal should not throw`() {
        val code =
            """
            let a: boolean = true;
            """.trimIndent()

        assertDoesNotThrow { parseCode(code) }
    }

    @Test
    fun `test BOOLEAN type with number literal should throw exception`() {
        val code =
            """
            let a: boolean = 8;
            """.trimIndent()

        assertThrows(Exception::class.java) { parseCode(code) }
    }

    @Test
    fun `test BOOLEAN type with string literal should throw exception`() {
        val code =
            """
            let a: boolean = "bad idea";
            """.trimIndent()

        assertThrows(Exception::class.java) { parseCode(code) }
    }

    @Test
    fun `test mixed expression with NUMBER types - should ignore parenthesis - valid case`() {
        val code =
            """
            let a: number = (5 + (3 * 2)) - (4 / 2) + 7;
            """.trimIndent()

        assertDoesNotThrow {
            parseCode(code)
        }
    }

    @Test
    fun `test mixed expression with NUMBER type - valid case`() {
        val code =
            """
            let a: number = 5 + 3 * 2;
            """.trimIndent()

        assertDoesNotThrow {
            parseCode(code)
        }
    }

    @Test
    fun `test mixed expression with NUMBER type - invalid case`() {
        val code =
            """
            let a: number = "5" + 3 * 2;
            """.trimIndent()

        assertThrows(Exception::class.java) { parseCode(code) }
    }

    @Test
    fun `test mixed expression with STRING type - valid case`() {
        val code =
            """
            let a: string = "hello" + "world";
            """.trimIndent()

        assertDoesNotThrow { parseCode(code) }
    }

    @Test
    fun `test mixed expression with STRING type - invalid case`() {
        val code =
            """
            let a: string = "hello" + 42;
            """.trimIndent()

        assertThrows(Exception::class.java) { parseCode(code) }
    }

    @Test
    fun `test with identifiers and operators should not throw for NUMBER type`() {
        val code =
            """
            let b: number = a + 3 * 2;
            """.trimIndent()

        assertDoesNotThrow {
            parseCode(code)
        }
    }

    @Test
    fun `test with identifiers and operators should not throw for STRING type`() {
        val code =
            """
            let b: string = a + "world";
            """.trimIndent()

        assertDoesNotThrow {
            parseCode(code)
        }
    }

    @Test
    fun `test complex expression with first invalid token for NUMBER type`() {
        val code =
            """
            let b: number = "world" + 5;
            """.trimIndent()

        assertThrows(Exception::class.java) { parseCode(code) }
    }

    @Test
    fun `test complex expression with last invalid token for BOOLEAN type`() {
        val code =
            """
            let b: boolean = true + 1;
            """.trimIndent()

        assertThrows(Exception::class.java) { parseCode(code) }
    }
}
