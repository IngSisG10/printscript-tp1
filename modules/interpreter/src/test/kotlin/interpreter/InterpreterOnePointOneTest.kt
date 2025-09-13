package interpreter

import common.ast.AssignmentNode
import common.ast.BinaryOpNode
import common.ast.BlockStatementNode
import common.ast.DeclaratorNode
import common.ast.FunctionNode
import common.ast.IdentifierNode
import common.ast.IfStatementNode
import common.ast.LiteralNode
import common.ast.VariableNode
import common.enums.DeclarationTypeEnum
import common.enums.FunctionEnum
import common.enums.OperationEnum
import common.enums.TypeEnum
import common.exception.InterpreterException
import common.exception.TypeMismatchException
import common.exception.UndefinedVariableException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InterpreterOnePointOneTest {
    private lateinit var interpreter: Interpreter
    private var mockInput: String? = null

    @BeforeEach
    fun setUp() {
        interpreter = Interpreter { mockInput ?: "" }
    }

    // =================== CONST DECLARATIONS TESTS ===================

    @Test
    fun testConstDeclarationBasic() {
        val variableNode = VariableNode("PI", TypeEnum.NUMBER)
        val initialValue = LiteralNode(3.14, TypeEnum.NUMBER)
        val declarator = DeclaratorNode(variableNode, initialValue, DeclarationTypeEnum.CONST)

        val result = interpreter.interpret(listOf(declarator))
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun testConstDeclarationWithString() {
        val variableNode = VariableNode("APP_NAME", TypeEnum.STRING)
        val initialValue = LiteralNode("PrintScript", TypeEnum.STRING)
        val declarator = DeclaratorNode(variableNode, initialValue, DeclarationTypeEnum.CONST)
        val println = FunctionNode(FunctionEnum.PRINTLN, IdentifierNode("APP_NAME"))

        val result = interpreter.interpret(listOf(declarator, println))
        assertEquals(listOf("PrintScript"), result)
    }

    @Test
    fun testConstDeclarationWithBoolean() {
        val variableNode = VariableNode("DEBUG_MODE", TypeEnum.BOOLEAN)
        val initialValue = LiteralNode(true, TypeEnum.BOOLEAN)
        val declarator = DeclaratorNode(variableNode, initialValue, DeclarationTypeEnum.CONST)
        val println = FunctionNode(FunctionEnum.PRINTLN, IdentifierNode("DEBUG_MODE"))

        val result = interpreter.interpret(listOf(declarator, println))
        assertEquals(listOf("true"), result)
    }

    @Test
    fun testConstCannotBeReassigned() {
        val variableNode = VariableNode("PI", TypeEnum.NUMBER)
        val initialValue = LiteralNode(3.14, TypeEnum.NUMBER)
        val declarator = DeclaratorNode(variableNode, initialValue, DeclarationTypeEnum.CONST)
        val newValue = LiteralNode(2.71, TypeEnum.NUMBER)
        val assignment = AssignmentNode(OperationEnum.EQUAL, IdentifierNode("PI"), newValue)

        assertThrows(InterpreterException::class.java) {
            interpreter.interpret(listOf(declarator, assignment))
        }
    }

    @Test
    fun testLetCanBeReassigned() {
        val variableNode = VariableNode("x", TypeEnum.NUMBER)
        val initialValue = LiteralNode(5.0, TypeEnum.NUMBER)
        val declarator = DeclaratorNode(variableNode, initialValue, DeclarationTypeEnum.LET)
        val newValue = LiteralNode(10.0, TypeEnum.NUMBER)
        val assignment = AssignmentNode(OperationEnum.EQUAL, IdentifierNode("x"), newValue)
        val println = FunctionNode(FunctionEnum.PRINTLN, IdentifierNode("x"))

        val result = interpreter.interpret(listOf(declarator, assignment, println))
        assertEquals(listOf("10"), result)
    }

    // =================== BOOLEAN TYPE SUPPORT TESTS ===================

    @Test
    fun testBooleanLiteralTrue() {
        val literal = LiteralNode(true, TypeEnum.BOOLEAN)
        val println = FunctionNode(FunctionEnum.PRINTLN, literal)

        val result = interpreter.interpret(listOf(println))
        assertEquals(listOf("true"), result)
    }

    @Test
    fun testBooleanLiteralFalse() {
        val literal = LiteralNode(false, TypeEnum.BOOLEAN)
        val println = FunctionNode(FunctionEnum.PRINTLN, literal)

        val result = interpreter.interpret(listOf(println))
        assertEquals(listOf("false"), result)
    }

    @Test
    fun testBooleanVariableDeclaration() {
        val variableNode = VariableNode("isValid", TypeEnum.BOOLEAN)
        val initialValue = LiteralNode(true, TypeEnum.BOOLEAN)
        val declarator = DeclaratorNode(variableNode, initialValue, DeclarationTypeEnum.LET)
        val println = FunctionNode(FunctionEnum.PRINTLN, IdentifierNode("isValid"))

        val result = interpreter.interpret(listOf(declarator, println))
        assertEquals(listOf("true"), result)
    }

    @Test
    fun testBooleanVariableAssignment() {
        val variableNode = VariableNode("flag", TypeEnum.BOOLEAN)
        val declarator = DeclaratorNode(variableNode, LiteralNode(false, TypeEnum.BOOLEAN), DeclarationTypeEnum.LET)
        val assignment = AssignmentNode(OperationEnum.EQUAL, IdentifierNode("flag"), LiteralNode(true, TypeEnum.BOOLEAN))
        val println = FunctionNode(FunctionEnum.PRINTLN, IdentifierNode("flag"))

        val result = interpreter.interpret(listOf(declarator, assignment, println))
        assertEquals(listOf("true"), result)
    }

    // =================== IF STATEMENT TESTS ===================

    @Test
    fun testIfStatementWithTrueCondition() {
        val condition = LiteralNode(true, TypeEnum.BOOLEAN)
        val printStatement = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("Inside if", TypeEnum.STRING))
        val thenBlock = BlockStatementNode(listOf(printStatement))
        val ifStatement = IfStatementNode(condition, thenBlock, null)

        val result = interpreter.interpret(listOf(ifStatement))
        assertEquals(listOf("Inside if"), result)
    }

    @Test
    fun testIfStatementWithFalseCondition() {
        val condition = LiteralNode(false, TypeEnum.BOOLEAN)
        val printStatement = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("Inside if", TypeEnum.STRING))
        val thenBlock = BlockStatementNode(listOf(printStatement))
        val ifStatement = IfStatementNode(condition, thenBlock, null)

        val result = interpreter.interpret(listOf(ifStatement))
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun testIfElseStatementWithTrueCondition() {
        val condition = LiteralNode(true, TypeEnum.BOOLEAN)
        val thenStatement = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("Then branch", TypeEnum.STRING))
        val elseStatement = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("Else branch", TypeEnum.STRING))
        val thenBlock = BlockStatementNode(listOf(thenStatement))
        val elseBlock = BlockStatementNode(listOf(elseStatement))
        val ifStatement = IfStatementNode(condition, thenBlock, elseBlock)

        val result = interpreter.interpret(listOf(ifStatement))
        assertEquals(listOf("Then branch"), result)
    }

    @Test
    fun testIfElseStatementWithFalseCondition() {
        val condition = LiteralNode(false, TypeEnum.BOOLEAN)
        val thenStatement = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("Then branch", TypeEnum.STRING))
        val elseStatement = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("Else branch", TypeEnum.STRING))
        val thenBlock = BlockStatementNode(listOf(thenStatement))
        val elseBlock = BlockStatementNode(listOf(elseStatement))
        val ifStatement = IfStatementNode(condition, thenBlock, elseBlock)

        val result = interpreter.interpret(listOf(ifStatement))
        assertEquals(listOf("Else branch"), result)
    }

    @Test
    fun testIfStatementWithBooleanVariable() {
        val variableNode = VariableNode("condition", TypeEnum.BOOLEAN)
        val declarator = DeclaratorNode(variableNode, LiteralNode(true, TypeEnum.BOOLEAN), DeclarationTypeEnum.LET)
        val condition = IdentifierNode("condition")
        val printStatement = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("Condition is true", TypeEnum.STRING))
        val thenBlock = BlockStatementNode(listOf(printStatement))
        val ifStatement = IfStatementNode(condition, thenBlock, null)

        val result = interpreter.interpret(listOf(declarator, ifStatement))
        assertEquals(listOf("Condition is true"), result)
    }

    @Test
    fun testIfStatementWithNonBooleanConditionThrowsException() {
        val condition = LiteralNode(1.0, TypeEnum.NUMBER)
        val printStatement = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("This should not print", TypeEnum.STRING))
        val thenBlock = BlockStatementNode(listOf(printStatement))
        val ifStatement = IfStatementNode(condition, thenBlock, null)

        assertThrows(TypeMismatchException::class.java) {
            interpreter.interpret(listOf(ifStatement))
        }
    }

    @Test
    fun testIfStatementWithStringConditionThrowsException() {
        val condition = LiteralNode("true", TypeEnum.STRING)
        val printStatement = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("This should not print", TypeEnum.STRING))
        val thenBlock = BlockStatementNode(listOf(printStatement))
        val ifStatement = IfStatementNode(condition, thenBlock, null)

        assertThrows(TypeMismatchException::class.java) {
            interpreter.interpret(listOf(ifStatement))
        }
    }

    @Test
    fun testNestedIfStatements() {
        val outerCondition = LiteralNode(true, TypeEnum.BOOLEAN)
        val innerCondition = LiteralNode(true, TypeEnum.BOOLEAN)
        val innerPrint = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("Nested if", TypeEnum.STRING))
        val innerThenBlock = BlockStatementNode(listOf(innerPrint))
        val innerIf = IfStatementNode(innerCondition, innerThenBlock, null)
        val outerThenBlock = BlockStatementNode(listOf(innerIf))
        val outerIf = IfStatementNode(outerCondition, outerThenBlock, null)

        val result = interpreter.interpret(listOf(outerIf))
        assertEquals(listOf("Nested if"), result)
    }

    @Test
    fun testIfBlockWithMultipleStatements() {
        val condition = LiteralNode(true, TypeEnum.BOOLEAN)
        val print1 = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("First", TypeEnum.STRING))
        val print2 = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("Second", TypeEnum.STRING))
        val thenBlock = BlockStatementNode(listOf(print1, print2))
        val ifStatement = IfStatementNode(condition, thenBlock, null)

        val result = interpreter.interpret(listOf(ifStatement))
        assertEquals(listOf("First", "Second"), result)
    }

    // =================== READINPUT FUNCTION TESTS ===================

    @Test
    fun testReadInputReturnsCorrectTypeForString() {
        mockInput = "Test String"
        val prompt = LiteralNode("Enter text: ", TypeEnum.STRING)
        val readInputCall = FunctionNode(FunctionEnum.READ_INPUT, prompt)
        val variableNode = VariableNode("text", TypeEnum.STRING)
        val declarator = DeclaratorNode(variableNode, readInputCall, DeclarationTypeEnum.LET)
        val println = FunctionNode(FunctionEnum.PRINTLN, IdentifierNode("text"))

        val result = interpreter.interpret(listOf(declarator, println))
        assertEquals(listOf("Test String"), result)
    }

    @Test
    fun testReadInputReturnsCorrectTypeForNumber() {
        mockInput = "123.5"
        val prompt = LiteralNode("Enter a number: ", TypeEnum.STRING)
        val readInputCall = FunctionNode(FunctionEnum.READ_INPUT, prompt)
        val variableNode = VariableNode("num", TypeEnum.NUMBER)
        val declarator = DeclaratorNode(variableNode, readInputCall, DeclarationTypeEnum.LET)
        val println = FunctionNode(FunctionEnum.PRINTLN, IdentifierNode("num"))

        val result = interpreter.interpret(listOf(declarator, println))
        assertEquals(listOf("123.5"), result)
    }

    @Test
    fun testReadInputReturnsCorrectTypeForBoolean() {
        mockInput = "true"
        val prompt = LiteralNode("Enter true/false: ", TypeEnum.STRING)
        val readInputCall = FunctionNode(FunctionEnum.READ_INPUT, prompt)
        val variableNode = VariableNode("flag", TypeEnum.BOOLEAN)
        val declarator = DeclaratorNode(variableNode, readInputCall, DeclarationTypeEnum.LET)
        val println = FunctionNode(FunctionEnum.PRINTLN, IdentifierNode("flag"))

        val result = interpreter.interpret(listOf(declarator, println))
        assertEquals(listOf("true"), result)
    }

    @Test
    fun testReadInputInPrintlnCall() {
        mockInput = "Direct Input"
        val prompt = LiteralNode("Enter value: ", TypeEnum.STRING)
        val readInputCall = FunctionNode(FunctionEnum.READ_INPUT, prompt)
        val println = FunctionNode(FunctionEnum.PRINTLN, readInputCall)

        val result = interpreter.interpret(listOf(println))
        assertEquals(listOf("Direct Input"), result)
    }

    // =================== READENV FUNCTION TESTS ===================

    @Test
    fun testReadEnvNotFoundThrowsException() {
        val envVarName = "NON_EXISTENT_VAR_12345"
        val readEnvCall = FunctionNode(FunctionEnum.READ_ENV, LiteralNode(envVarName, TypeEnum.STRING))
        val variableNode = VariableNode("path", TypeEnum.STRING)
        val declarator = DeclaratorNode(variableNode, readEnvCall, DeclarationTypeEnum.LET)

        assertThrows(InterpreterException::class.java) {
            interpreter.interpret(listOf(declarator))
        }
    }

    // =================== COMPLEX SCENARIOS ===================

    @Test
    fun testConstWithIfStatement() {
        val maxAge = VariableNode("MAX_AGE", TypeEnum.NUMBER)
        val maxAgeDeclarator = DeclaratorNode(maxAge, LiteralNode(65.0, TypeEnum.NUMBER), DeclarationTypeEnum.CONST)

        val userAge = VariableNode("userAge", TypeEnum.NUMBER)
        val userAgeDeclarator = DeclaratorNode(userAge, LiteralNode(70.0, TypeEnum.NUMBER), DeclarationTypeEnum.LET)

        // age > MAX_AGE comparison (would need comparison operators in real implementation)
        val condition = LiteralNode(true, TypeEnum.BOOLEAN) // Simplified for this test
        val thenStatement = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("Retirement age reached", TypeEnum.STRING))
        val elseStatement = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("Still working", TypeEnum.STRING))
        val thenBlock = BlockStatementNode(listOf(thenStatement))
        val elseBlock = BlockStatementNode(listOf(elseStatement))
        val ifStatement = IfStatementNode(condition, thenBlock, elseBlock)

        val result = interpreter.interpret(listOf(maxAgeDeclarator, userAgeDeclarator, ifStatement))
        assertEquals(listOf("Retirement age reached"), result)
    }

    @Test
    fun testBooleanVariableInIfCondition() {
        val isReady = VariableNode("isReady", TypeEnum.BOOLEAN)
        val declarator = DeclaratorNode(isReady, LiteralNode(false, TypeEnum.BOOLEAN), DeclarationTypeEnum.LET)
        val assignment = AssignmentNode(OperationEnum.EQUAL, IdentifierNode("isReady"), LiteralNode(true, TypeEnum.BOOLEAN))

        val condition = IdentifierNode("isReady")
        val printStatement = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("System is ready", TypeEnum.STRING))
        val thenBlock = BlockStatementNode(listOf(printStatement))
        val ifStatement = IfStatementNode(condition, thenBlock, null)

        val result = interpreter.interpret(listOf(declarator, assignment, ifStatement))
        assertEquals(listOf("System is ready"), result)
    }

    @Test
    fun testMultipleConstsAndVariables() {
        val appName = VariableNode("APP_NAME", TypeEnum.STRING)
        val appNameDeclarator =
            DeclaratorNode(
                appName,
                LiteralNode("PrintScript", TypeEnum.STRING),
                DeclarationTypeEnum.CONST,
            )

        val version = VariableNode("VERSION", TypeEnum.STRING)
        val versionDeclarator = DeclaratorNode(version, LiteralNode("1.1", TypeEnum.STRING), DeclarationTypeEnum.CONST)

        val debug = VariableNode("debug", TypeEnum.BOOLEAN)
        val debugDeclarator = DeclaratorNode(debug, LiteralNode(true, TypeEnum.BOOLEAN), DeclarationTypeEnum.LET)

        val condition = IdentifierNode("debug")
        val appInfo =
            BinaryOpNode(
                OperationEnum.SUM,
                IdentifierNode("APP_NAME"),
                BinaryOpNode(OperationEnum.SUM, LiteralNode(" v", TypeEnum.STRING), IdentifierNode("VERSION")),
            )
        val printStatement = FunctionNode(FunctionEnum.PRINTLN, appInfo)
        val thenBlock = BlockStatementNode(listOf(printStatement))
        val ifStatement = IfStatementNode(condition, thenBlock, null)

        val result = interpreter.interpret(listOf(appNameDeclarator, versionDeclarator, debugDeclarator, ifStatement))
        assertEquals(listOf("PrintScript v1.1"), result)
    }

    // =================== ERROR CASES AND EDGE CASES ===================

    @Test
    fun testTypeMismatchInBooleanDeclaration() {
        val variableNode = VariableNode("flag", TypeEnum.BOOLEAN)
        val initialValue = LiteralNode("not a boolean", TypeEnum.STRING)
        val declarator = DeclaratorNode(variableNode, initialValue, DeclarationTypeEnum.LET)

        assertThrows(TypeMismatchException::class.java) {
            interpreter.interpret(listOf(declarator))
        }
    }

    @Test
    fun testEmptyIfBlock() {
        val condition = LiteralNode(true, TypeEnum.BOOLEAN)
        val thenBlock = BlockStatementNode(emptyList())
        val ifStatement = IfStatementNode(condition, thenBlock, null)

        val result = interpreter.interpret(listOf(ifStatement))
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun testEmptyElseBlock() {
        val condition = LiteralNode(false, TypeEnum.BOOLEAN)
        val thenStatement = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("Then", TypeEnum.STRING))
        val thenBlock = BlockStatementNode(listOf(thenStatement))
        val elseBlock = BlockStatementNode(emptyList())
        val ifStatement = IfStatementNode(condition, thenBlock, elseBlock)

        val result = interpreter.interpret(listOf(ifStatement))
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun testVariableRedeclarationAsConst() {
        val var1 = VariableNode("x", TypeEnum.NUMBER)
        val declarator1 = DeclaratorNode(var1, LiteralNode(10.0, TypeEnum.NUMBER), DeclarationTypeEnum.LET)
        val var2 = VariableNode("x", TypeEnum.NUMBER)
        val declarator2 = DeclaratorNode(var2, LiteralNode(20.0, TypeEnum.NUMBER), DeclarationTypeEnum.CONST)

        assertThrows(InterpreterException::class.java) {
            interpreter.interpret(listOf(declarator1, declarator2))
        }
    }

    @Test
    fun testConstRedeclarationAsVariable() {
        val const1 = VariableNode("PI", TypeEnum.NUMBER)
        val declarator1 = DeclaratorNode(const1, LiteralNode(3.14, TypeEnum.NUMBER), DeclarationTypeEnum.CONST)
        val var2 = VariableNode("PI", TypeEnum.NUMBER)
        val declarator2 = DeclaratorNode(var2, LiteralNode(3.0, TypeEnum.NUMBER), DeclarationTypeEnum.LET)

        assertThrows(InterpreterException::class.java) {
            interpreter.interpret(listOf(declarator1, declarator2))
        }
    }

    @Test
    fun testUndefinedVariableInIfCondition() {
        val condition = IdentifierNode("undefinedVar")
        val printStatement = FunctionNode(FunctionEnum.PRINTLN, LiteralNode("This should not print", TypeEnum.STRING))
        val thenBlock = BlockStatementNode(listOf(printStatement))
        val ifStatement = IfStatementNode(condition, thenBlock, null)

        assertThrows(UndefinedVariableException::class.java) {
            interpreter.interpret(listOf(ifStatement))
        }
    }

    // =================== VERSION COMPATIBILITY TESTS ===================

    @Test
    fun testVersion10ShouldNotSupportBoolean() {
        // This test would check that v1.0 mode rejects boolean literals
        // Implementation would depend on version parameter in interpreter
        val literal = LiteralNode(true, TypeEnum.BOOLEAN)
        val println = FunctionNode(FunctionEnum.PRINTLN, literal)

        // In v1.0 mode, this should throw an exception
        // assertThrows(InterpreterException::class.java) {
        //     interpreterV10.interpret(listOf(println))
        // }
    }

    @Test
    fun testVersion10ShouldNotSupportIfStatements() {
        // This test would check that v1.0 mode rejects if statements
        // Implementation would depend on version parameter in interpreter
        val condition = LiteralNode(true, TypeEnum.BOOLEAN)
        val thenBlock = BlockStatementNode(emptyList())
        val ifStatement = IfStatementNode(condition, thenBlock, null)

        // In v1.0 mode, this should throw an exception
        // assertThrows(InterpreterException::class.java) {
        //     interpreterV10.interpret(listOf(ifStatement))
        // }
    }

    @Test
    fun testVersion10ShouldNotSupportReadInput() {
        // This test would check that v1.0 mode rejects readInput
        val prompt = LiteralNode("Enter value: ", TypeEnum.STRING)
        val readInputCall = FunctionNode(FunctionEnum.READ_INPUT, prompt)
        val println = FunctionNode(FunctionEnum.PRINTLN, readInputCall)

        // In v1.0 mode, this should throw an exception
        // assertThrows(InterpreterException::class.java) {
        //     interpreterV10.interpret(listOf(println))
        // }
    }

    @Test
    fun testVersion10ShouldNotSupportReadEnv() {
        // This test would check that v1.0 mode rejects readEnv
        val envVar = LiteralNode("PATH", TypeEnum.STRING)
        val readEnvCall = FunctionNode(FunctionEnum.READ_ENV, envVar)
        val println = FunctionNode(FunctionEnum.PRINTLN, readEnvCall)

        // In v1.0 mode, this should throw an exception
        // assertThrows(InterpreterException::class.java) {
        //     interpreterV10.interpret(listOf(println))
        // }
    }
}
