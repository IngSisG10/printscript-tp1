package parser

import ast.BinaryOpNode
import ast.DeclaratorNode
import ast.LiteralNode
import ast.VariableNode
import ast.abs.AstInterface
import enums.FunctionEnum
import lexer.Lexer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class AstStructureTest {
    @Test
    fun `test declarator node structure`() {
        val code = "let a: Number = 5;"
        val lexer = Lexer(code)
        val tokens = lexer.lex()
        val parser = Parser(tokens)
        val ast = parser.parse()

        assertEquals(1, ast.size)
        val node = ast[0]
        assertEquals("DeclaratorNode", node.javaClass.simpleName)

        // Verificar que tiene los campos correctos usando reflection
        val fields = node.javaClass.declaredFields.map { it.name }
        assertTrue(fields.contains("variableNode"))
        assertTrue(fields.contains("value"))
    }

    private fun containsBinaryOpNode(node: AstInterface): Boolean =
        when (node) {
            is BinaryOpNode -> true
            is DeclaratorNode -> containsBinaryOpNode(node.value)
            is VariableNode -> false
            is LiteralNode -> false
            else -> false
        }

    @Test
    fun `test binary operation node structure`() {
        val code = "let result: Number = 2 + 3;"
        val lexer = Lexer(code)
        val tokens = lexer.lex()
        val parser = Parser(tokens)
        val ast = parser.parse()

        println(ast)

        val containsBinaryOp = ast.any { containsBinaryOpNode(it) }
        assertTrue(containsBinaryOp)
    }

    @Test
    fun `test function call node structure`() {
        val code = "println(\"test\");"
        val lexer = Lexer(code)
        val tokens = lexer.lex()
        val parser = Parser(tokens)
        val ast = parser.parse()

        assertEquals(1, ast.size)
        val node = ast[0]
        assertEquals("FunctionNode", node.javaClass.simpleName)

        val functionNameField = node.javaClass.getDeclaredField("functionName")
        functionNameField.isAccessible = true
        val functionName = functionNameField.get(node) as FunctionEnum
        assertEquals(FunctionEnum.PRINTLN, functionName)
    }
}
