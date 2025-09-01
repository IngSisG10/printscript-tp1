package interpreter

import ast.AssignmentNode
import ast.BinaryOpNode
import ast.DeclaratorNode
import ast.FunctionNode
import ast.IdentifierNode
import ast.LiteralNode
import ast.VariableNode
import enums.FunctionEnum
import enums.OperationEnum
import enums.TypeEnum
import exception.DivisionByZeroException
import exception.InterpreterException
import exception.TypeMismatchException
import exception.UndefinedVariableException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PrintScriptInterpreterTest {
    private lateinit var interpreter: PrintScriptInterpreter

    @BeforeEach
    fun setUp() {
        interpreter = PrintScriptInterpreter()
    }

    @Test
    fun testNumberLiteral() {
        val literal = LiteralNode(42.0, TypeEnum.NUMBER)
        val result = interpreter.interpret(listOf(literal))
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun testStringLiteral() {
        val literal = LiteralNode("Hello, World!", TypeEnum.STRING)
        val result = interpreter.interpret(listOf(literal))
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun testBooleanLiteralNotSupported() {
        val literal = LiteralNode(true, TypeEnum.BOOLEAN)
        assertThrows(InterpreterException::class.java) {
            interpreter.interpret(listOf(literal))
        }
    }

    @Test
    fun testBinaryOpAdditionNumbers() {
        val left = LiteralNode(10.0, TypeEnum.NUMBER)
        val right = LiteralNode(5.0, TypeEnum.NUMBER)
        val addition = BinaryOpNode(OperationEnum.SUM, left, right)
        val println = FunctionNode(FunctionEnum.PRINTLN, addition)

        val result = interpreter.interpret(listOf(println))
        assertEquals(listOf("15.0"), result)
    }

    @Test
    fun testBinaryOpAdditionStrings() {
        val left = LiteralNode("Hello, ", TypeEnum.STRING)
        val right = LiteralNode("World!", TypeEnum.STRING)
        val addition = BinaryOpNode(OperationEnum.SUM, left, right)
        val println = FunctionNode(FunctionEnum.PRINTLN, addition)

        val result = interpreter.interpret(listOf(println))
        assertEquals(listOf("Hello, World!"), result)
    }

    @Test
    fun testBinaryOpAdditionStringAndNumber() {
        val left = LiteralNode("Value: ", TypeEnum.STRING)
        val right = LiteralNode(42.0, TypeEnum.NUMBER)
        val addition = BinaryOpNode(OperationEnum.SUM, left, right)
        val println = FunctionNode(FunctionEnum.PRINTLN, addition)

        val result = interpreter.interpret(listOf(println))
        assertEquals(listOf("Value: 42.0"), result)
    }

    @Test
    fun testBinaryOpSubtraction() {
        val left = LiteralNode(10.0, TypeEnum.NUMBER)
        val right = LiteralNode(3.0, TypeEnum.NUMBER)
        val subtraction = BinaryOpNode(OperationEnum.MINUS, left, right)
        val println = FunctionNode(FunctionEnum.PRINTLN, subtraction)

        val result = interpreter.interpret(listOf(println))
        assertEquals(listOf("7.0"), result)
    }

    @Test
    fun testBinaryOpMultiplication() {
        val left = LiteralNode(4.0, TypeEnum.NUMBER)
        val right = LiteralNode(3.0, TypeEnum.NUMBER)
        val multiplication = BinaryOpNode(OperationEnum.MULTIPLY, left, right)
        val println = FunctionNode(FunctionEnum.PRINTLN, multiplication)

        val result = interpreter.interpret(listOf(println))
        assertEquals(listOf("12.0"), result)
    }

    @Test
    fun testBinaryOpDivision() {
        val left = LiteralNode(15.0, TypeEnum.NUMBER)
        val right = LiteralNode(3.0, TypeEnum.NUMBER)
        val division = BinaryOpNode(OperationEnum.DIVIDE, left, right)
        val println = FunctionNode(FunctionEnum.PRINTLN, division)

        val result = interpreter.interpret(listOf(println))
        assertEquals(listOf("5.0"), result)
    }

    @Test
    fun testDivisionByZero() {
        val left = LiteralNode(10.0, TypeEnum.NUMBER)
        val right = LiteralNode(0.0, TypeEnum.NUMBER)
        val division = BinaryOpNode(OperationEnum.DIVIDE, left, right)

        assertThrows(DivisionByZeroException::class.java) {
            interpreter.interpret(listOf(division))
        }
    }

    @Test
    fun testSubtractionWithStringsThrowsException() {
        val left = LiteralNode("Hello", TypeEnum.STRING)
        val right = LiteralNode("World", TypeEnum.STRING)
        val subtraction = BinaryOpNode(OperationEnum.MINUS, left, right)

        assertThrows(TypeMismatchException::class.java) {
            interpreter.interpret(listOf(subtraction))
        }
    }

    @Test
    fun testMultiplicationWithStringsThrowsException() {
        val left = LiteralNode("Hello", TypeEnum.STRING)
        val right = LiteralNode("World", TypeEnum.STRING)
        val multiplication = BinaryOpNode(OperationEnum.MULTIPLY, left, right)

        assertThrows(TypeMismatchException::class.java) {
            interpreter.interpret(listOf(multiplication))
        }
    }

    @Test
    fun testVariableDeclarationWithInitialization() {
        val variableNode = VariableNode("x", TypeEnum.NUMBER)
        val initialValue = LiteralNode(42.0, TypeEnum.NUMBER)
        val declarator = DeclaratorNode(variableNode, initialValue)

        val result = interpreter.interpret(listOf(declarator))
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun testVariableAssignment() {
        val variableNode = VariableNode("x", TypeEnum.NUMBER)
        val initialValue = LiteralNode(5.0, TypeEnum.NUMBER)
        val declarator = DeclaratorNode(variableNode, initialValue)
        val newValue = LiteralNode(10.0, TypeEnum.NUMBER)
        val assignment = AssignmentNode(OperationEnum.EQUAL, IdentifierNode("x"), newValue)

        val result = interpreter.interpret(listOf(declarator, assignment))
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun testVariableUsage() {
        val variableNode = VariableNode("x", TypeEnum.NUMBER)
        val initialValue = LiteralNode(42.0, TypeEnum.NUMBER)
        val declarator = DeclaratorNode(variableNode, initialValue)
        val identifier = IdentifierNode("x")
        val println = FunctionNode(FunctionEnum.PRINTLN, identifier)

        val result = interpreter.interpret(listOf(declarator, println))
        assertEquals(listOf("42.0"), result)
    }

    @Test
    fun testUndefinedVariableThrowsException() {
        val identifier = IdentifierNode("undefined")

        assertThrows(UndefinedVariableException::class.java) {
            interpreter.interpret(listOf(identifier))
        }
    }

    @Test
    fun testTypeMismatchInDeclaration() {
        val variableNode = VariableNode("x", TypeEnum.NUMBER)
        val initialValue = LiteralNode("not a number", TypeEnum.STRING)
        val declarator = DeclaratorNode(variableNode, initialValue)

        assertThrows(TypeMismatchException::class.java) {
            interpreter.interpret(listOf(declarator))
        }
    }

    @Test
    fun testTypeMismatchInAssignment() {
        val variableNode = VariableNode("x", TypeEnum.NUMBER)
        val declarator = DeclaratorNode(variableNode, LiteralNode(10.0, TypeEnum.NUMBER))
        val stringValue = LiteralNode("not a number", TypeEnum.STRING)
        val assignment = AssignmentNode(OperationEnum.EQUAL, IdentifierNode("x"), stringValue)

        assertThrows(TypeMismatchException::class.java) {
            interpreter.interpret(listOf(declarator, assignment))
        }
    }

    @Test
    fun testVariableRedeclarationThrowsException() {
        val variableNode1 = VariableNode("x", TypeEnum.NUMBER)
        val declarator1 = DeclaratorNode(variableNode1, LiteralNode(10.0, TypeEnum.NUMBER))
        val variableNode2 = VariableNode("x", TypeEnum.STRING)
        val declarator2 = DeclaratorNode(variableNode2, LiteralNode("hello", TypeEnum.STRING))

        assertThrows(InterpreterException::class.java) {
            interpreter.interpret(listOf(declarator1, declarator2))
        }
    }

    @Test
    fun testComplexExpression() {
        val var1 = VariableNode("a", TypeEnum.NUMBER)
        val var2 = VariableNode("b", TypeEnum.NUMBER)
        val decl1 = DeclaratorNode(var1, LiteralNode(10.0, TypeEnum.NUMBER))
        val decl2 = DeclaratorNode(var2, LiteralNode(5.0, TypeEnum.NUMBER))

        val multiplication = BinaryOpNode(OperationEnum.MULTIPLY, IdentifierNode("a"), IdentifierNode("b"))
        val addition = BinaryOpNode(OperationEnum.SUM, multiplication, LiteralNode(2.0, TypeEnum.NUMBER))
        val println = FunctionNode(FunctionEnum.PRINTLN, addition)

        val result = interpreter.interpret(listOf(decl1, decl2, println))
        assertEquals(listOf("52.0"), result)
    }

    @Test
    fun testMultipleStatements() {
        val var1 = VariableNode("message", TypeEnum.STRING)
        val decl1 = DeclaratorNode(var1, LiteralNode("Hello", TypeEnum.STRING))
        val println1 = FunctionNode(FunctionEnum.PRINTLN, IdentifierNode("message"))

        val assignment = AssignmentNode(OperationEnum.EQUAL, IdentifierNode("message"), LiteralNode("World", TypeEnum.STRING))
        val println2 = FunctionNode(FunctionEnum.PRINTLN, IdentifierNode("message"))

        val result = interpreter.interpret(listOf(decl1, println1, assignment, println2))
        assertEquals(listOf("Hello", "World"), result)
    }

    @Test
    fun testPrintlnWithStringConcatenation() {
        val greeting = LiteralNode("Hello, ", TypeEnum.STRING)
        val name = LiteralNode("PrintScript!", TypeEnum.STRING)
        val concatenation = BinaryOpNode(OperationEnum.SUM, greeting, name)
        val println = FunctionNode(FunctionEnum.PRINTLN, concatenation)

        val result = interpreter.interpret(listOf(println))
        assertEquals(listOf("Hello, PrintScript!"), result)
    }

    @Test
    fun testAnyTypeWithNumber() {
        val variableNode = VariableNode("x", TypeEnum.ANY)
        val initialValue = LiteralNode(42.0, TypeEnum.ANY)
        val declarator = DeclaratorNode(variableNode, initialValue)
        val println = FunctionNode(FunctionEnum.PRINTLN, IdentifierNode("x"))

        val result = interpreter.interpret(listOf(declarator, println))
        assertEquals(listOf("42.0"), result)
    }

    @Test
    fun testAnyTypeWithString() {
        val variableNode = VariableNode("x", TypeEnum.ANY)
        val initialValue = LiteralNode("Hello", TypeEnum.ANY)
        val declarator = DeclaratorNode(variableNode, initialValue)
        val println = FunctionNode(FunctionEnum.PRINTLN, IdentifierNode("x"))

        val result = interpreter.interpret(listOf(declarator, println))
        assertEquals(listOf("Hello"), result)
    }

    @Test
    fun testInvalidNumberLiteral() {
        val literal = LiteralNode("invalid", TypeEnum.NUMBER)

        assertThrows(InterpreterException::class.java) {
            interpreter.interpret(listOf(literal))
        }
    }

    @Test
    fun testUnsupportedAssignmentOperator() {
        val variableNode = VariableNode("x", TypeEnum.NUMBER)
        val declarator = DeclaratorNode(variableNode, LiteralNode(10.0, TypeEnum.NUMBER))
        val assignment = AssignmentNode(OperationEnum.SUM, IdentifierNode("x"), LiteralNode(5.0, TypeEnum.NUMBER))

        assertThrows(InterpreterException::class.java) {
            interpreter.interpret(listOf(declarator, assignment))
        }
    }
}
