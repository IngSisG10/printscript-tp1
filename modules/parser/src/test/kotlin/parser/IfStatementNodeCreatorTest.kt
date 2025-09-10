package parser
import lexer.Lexer
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class IfStatementNodeCreatorTest {
    private fun parseCode(code: String): List<Any> {
        val lexer = Lexer()
        val tokens = lexer.lex(code)
        val parser = Parser()
        return parser.parse(tokens)
    }

//    @Test
//    fun `parses if statement without else`() {
//        val code =
//            """
//            if (true) {
//                1
//            }
//            """.trimIndent()
//        val ast = parseCode(code)
//        assertTrue(ast.first() is common.ast.IfStatementNode)
//        assertNull((ast.first() as common.ast.IfStatementNode).elseBody)
//    }
//
//    @Test
//    fun `parses if statement with else`() {
//        val code =
//            """
//            if (false) {
//                2
//            } else {
//                3
//            }
//            """.trimIndent()
//        val ast = parseCode(code)
//        assertTrue(ast.first() is common.ast.IfStatementNode)
//        assertNotNull((ast.first() as common.ast.IfStatementNode).elseBody)
//    }

    @Test
    fun `throws exception for missing closing brace`() {
        val code =
            """
            if (true) {
                1
            """.trimIndent()
        // Missing closing brace
        assertThrows(Exception::class.java) { parseCode(code) }
    }
}
