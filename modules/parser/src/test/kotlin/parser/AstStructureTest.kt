package parser

import common.ast.AstNode
import common.ast.BinaryOpNode
import common.ast.DeclaratorNode
import common.ast.LiteralNode
import common.ast.VariableNode
import common.enums.FunctionEnum
import lexer.Lexer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class AstStructureTest {
    private fun parseCode(code: String): List<AstNode> {
        val lexer = Lexer()
        val tokens = lexer.lex(code)
        val parser = Parser()
        return parser.parse(tokens)
    }

    @Test
    fun `test declarator node structure`() {
        val code = "let a: number = 5;"
        val ast = parseCode(code)

        assertEquals(1, ast.size)
        val node = ast[0]
        assertEquals("DeclaratorNode", node.javaClass.simpleName)

        // Verificar que tiene los campos correctos usando reflection
        val fields = node.javaClass.declaredFields.map { it.name }
        assertTrue(fields.contains("variableNode"))
        assertTrue(fields.contains("value"))
    }

    private fun containsBinaryOpNode(node: AstNode): Boolean =
        when (node) {
            is BinaryOpNode -> true
            is DeclaratorNode -> containsBinaryOpNode(node.value)
            is VariableNode -> false
            is LiteralNode -> false
            else -> false
        }

    @Test
    fun `test binary operation node structure`() {
        val code = "let result: number = 2 + 3;"
        val ast = parseCode(code)

        println(ast)

        val containsBinaryOp = ast.any { containsBinaryOpNode(it) }
        assertTrue(containsBinaryOp)
    }

    @Test
    fun `test function call node structure`() {
        val code = "println(\"test\");"
        val ast = parseCode(code)

        assertEquals(1, ast.size)
        val node = ast[0]
        assertEquals("FunctionNode", node.javaClass.simpleName)

        val functionNameField = node.javaClass.getDeclaredField("functionName")
        functionNameField.isAccessible = true
        val functionName = functionNameField.get(node) as FunctionEnum
        assertEquals(FunctionEnum.PRINTLN, functionName)
    }
}
