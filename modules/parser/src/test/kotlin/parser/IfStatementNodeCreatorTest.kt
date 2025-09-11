package parser
import common.ast.DeclaratorNode
import common.ast.IfStatementNode
import common.exception.UnrecognizedLineException
import lexer.util.LexerUtil
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class IfStatementNodeCreatorTest {
    private fun parseCode(code: String): List<Any> {
        val lexer = LexerUtil.createLexer("1.1")
        val tokens = lexer.lex(code)
        val parser = Parser()
        return parser.parse(tokens)
    }

    @Test
    fun `parses if statement without else`() {
        val code =
            """
            if (true) {
                1;
            }
            """.trimIndent()
        val ast = parseCode(code)
        assertTrue(ast.first() is IfStatementNode)
        assertNull((ast.first() as IfStatementNode).elseBlock)
    }

    @Test
    fun `parses if statement with else`() {
        val code =
            """
            if (false) {
                2;
            } else {
                3;
            }
            """.trimIndent()
        val ast = parseCode(code)
        assertTrue(ast.first() is IfStatementNode)
        assertNotNull((ast.first() as IfStatementNode).elseBlock)
    }

    @Test
    fun `throws exception for missing closing brace for if block`() {
        val code =
            """
            if (true) {
                1
            """.trimIndent()
        // Missing closing brace
        assertThrows(UnrecognizedLineException::class.java) { parseCode(code) }
    }

    @Test
    fun `throws exception when using == in condition`() {
        val code =
            """
            if (a == 1) {
                1
            
            """.trimIndent()
        // Missing closing brace
        assertThrows(UnrecognizedLineException::class.java) { parseCode(code) }
    }

    @Test
    fun `throws exception for missing closing brace for else block`() {
        val code =
            """
            if (true) {
                1
            } else {
                2
            """.trimIndent()
        // Missing closing brace
        assertThrows(UnrecognizedLineException::class.java) { parseCode(code) }
    }

    @Test
    fun `parses if statement with Identifier `() {
        val code =
            """
            let a: boolean = true;    
            if (a) {
                2;
            } else {
                3;
            }
            let b : number = 5;
            """.trimIndent()
        val ast = parseCode(code)
        assertTrue(ast.size == 3)
        assertTrue(ast.first() is DeclaratorNode)
        assertTrue(ast[1] is IfStatementNode)
        assertNotNull((ast[1] as IfStatementNode).elseBlock)
    }

    @Test
    fun `parses if statement with else if (throws an error) `() {
        val code =
            """
            let a: Boolean = true;    
            if (a) {
                2;
            } else if (false){
                3;
            }
            let b : Number = 5;
            """.trimIndent()
        // else if invalid
        assertThrows(UnrecognizedLineException::class.java) { parseCode(code) }
    }
}
