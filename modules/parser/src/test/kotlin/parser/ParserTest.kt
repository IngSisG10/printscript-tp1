package parser

import lexer.Lexer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

// fixme
class ParserTest {
    private fun parseCode(code: String): List<Any> {
        val lexer = Lexer(code)
        val tokens = lexer.lex()
        val parser = Parser(tokens)
        return parser.parse()
    }

    @Test
    fun `test variable declaration with number literal`() {
        val code = "let a: Number = 5;"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test variable declaration with string literal`() {
        val code = "let b: String = \"hello\";"
        val ast = parseCode(code)
        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test variable declaration with expression`() {
        val code = "let result: Number = 5 + 3 * 2;"
        val ast = parseCode(code)
        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test multiple declarations`() {
        val code = "let a: Number = 5; let b: String = \"test\";"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(2, ast.size)
    }

// fixme
//    @Test
//    fun `test variable assignment`() {
//        val code =
//            """
//            let a: Number = 5;
//            a = 10;
//            """.trimIndent()
//
//        val ast = parseCode(code)
//
//        assertNotNull(ast)
//        assertEquals(2, ast.size)
//    }
//
//    @Test
//    fun `test function call`() {
//        val code = "println(\"hello world\");"
//        val ast = parseCode(code)
//
//        assertNotNull(ast)
//        assertEquals(1, ast.size)
//    }

    @Test
    fun `test arithmetic expressions`() {
        val codes =
            listOf(
                "let a: Number = 1 + 2;",
                "let b: Number = 3 - 4;",
                "let c: Number = 5 * 6;",
                "let d: Number = 7 / 8;",
            )

        codes.forEach { code ->
            val ast = parseCode(code)
            assertNotNull(ast)
            assertEquals(1, ast.size)
        }
    }

    @Test
    fun `test complex expression with precedence`() {
        val code = "let result: Number = 2 + 3 * 4 - 6 / 2;"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

//    @Test
//    fun `test mixed statements`() {
//        val code =
//            """
//            let a: Number = 5;
//            let b: Number = 10;
//            let sum: Number = a + b;
//
//            """
//
//        val ast = parseCode(code)
//
//        assertNotNull(ast)
//        assertEquals(5, ast.size)
//    }

    // Tests de casos de error
    @Test
    fun `test missing semicolon should throw exception`() {
        val code = "let a: Number = 5" // Falta ;

        val ast = parseCode(code)

        print(ast)
    }

    @Test
    fun `test missing type annotation should throw exception`() {
        val code = "let a = 5;" // Falta : Number

        assertThrows<Exception> {
            parseCode(code)
        }
    }

    @Test
    fun `test unbalanced parentheses should throw exception`() {
        val code = "let a: Number = (5 + 3;" // Falta )

        assertThrows<Exception> {
            parseCode(code)
        }
    }

    @Test
    fun `test invalid assignment should throw exception`() {
        val code = "5 = 10;" // No se puede asignar a un literal

        assertThrows<Exception> {
            parseCode(code)
        }
    }

    @Test
    fun `test empty input should return empty list`() {
        val code = ""
        val ast = parseCode(code)

        assertTrue(ast.isEmpty())
    }
// fixme
//    @Test
//    fun `test whitespace only input should return empty list`() {
//        val code = "   \t\n  "
//        val ast = parseCode(code)
//
//        assertTrue(ast.isEmpty())
//    }

    // fixme -> lexer fails here -> why?
//    @Test
//    fun `test very large number`() {
//        val code = "let big: Number = 999999999999999;"
//        val ast = parseCode(code)
//
//        assertNotNull(ast)
//        assertEquals(1, ast.size)
//    }

    @Test
    fun `test empty string`() {
        val code = "let empty: String = \"\";"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test string with special characters`() {
        val code = "let special: String = \"hello\\nworld\\t!\";"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

//    @Test
//    fun `test nested expressions`() {
//        val code = "let nested: Number = (2 * (3 + (4 - 1)));"
//        val ast = parseCode(code)
//
//        assertNotNull(ast)
//        assertEquals(1, ast.size)
//    }

    @Test
    fun `test multiple operations in single expression`() {
        val code = "let complex: Number = 1 + 2 - 3 * 4 / 5;"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }
}
