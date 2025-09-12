package parser

import common.ast.DeclaratorNode
import common.ast.FunctionNode
import common.enums.FunctionEnum
import lexer.util.LexerUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Test
import kotlin.test.assertTrue

class ParserTestOnePointOne {
    private fun parseCode(code: String): List<Any> {
        val lexer = LexerUtil.createLexer("1.1")
        val tokens = lexer.lex(code)
        val parser = Parser()
        return parser.parse(tokens)
    }

    @Test
    fun `test variable declaration with const - number literal`() {
        val code = "const a : number = 5;"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test const declaration with string literal`() {
        val code = "const message : string = \"Hello World\";"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test const declaration with boolean literal`() {
        val code = "const isActive : boolean = true;"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test const declaration with expression`() {
        val code = "const result : number = 10 + 5 * 2;"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test multiple const declarations`() {
        val code =
            """
            const pi : number = 3.14;
            const name : string = "John";
            const isValid : boolean = false;
            """.trimIndent()
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(3, ast.size)
    }

    @Test
    fun `test const with negative number`() {
        val code = "const temperature : number = -25.5;"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test const with empty string`() {
        val code = "const empty : string = \"\";"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test readEnv function call`() {
        val code = "let envVar: string = readEnv(\"PATH\");"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test readEnv with different environment variables`() {
        val envVars = listOf("HOME", "USER", "JAVA_HOME", "NODE_PATH")

        envVars.forEach { envVar ->
            val code = "let env: string = readEnv(\"$envVar\");"
            val ast = parseCode(code)
            assertNotNull(ast)
            assertEquals(1, ast.size)
        }
    }

    @Test
    fun `test readEnv assigned to const`() {
        val code = "const homePath: string = readEnv(\"HOME\");"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test readInput assigned to const`() {
        val code = "const userResponse: string = readInput(\"\");"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test mixed let and const declarations`() {
        val code =
            """
            let variable: number = 10;
            const consTant: number = 20;
            variable = 30;
            """.trimIndent()
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(3, ast.size)
    }

    @Test
    fun `test readInput function call`() {
        val code = "let userInput: string = readInput(\"\");"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test complex arithmetic with const`() {
        val code = "const calculation: number = (5 + 3) * 2 - 4 / 2;"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test string concatenation with const`() {
        val code = "const fullName: string = \"John\" + \" \" + \"Doe\";"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test boolean expressions with const`() {
        val codes =
            listOf(
                "const isTrue: boolean = true;",
                "const isFalse: boolean = false;",
            )

        codes.forEach { code ->
            val ast = parseCode(code)
            assertNotNull(ast)
            assertEquals(1, ast.size)
        }
    }

    @Test
    fun `test function call with complex string parameter`() {
        val code = "let value: string = readEnv(\"PREFIX_\" + \"SUFFIX\");"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test all types with const declarations`() {
        val code =
            """
            const num: number = 42;
            const str: string = "test";
            const bool: boolean = true;
            """.trimIndent()
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(3, ast.size)
    }

    @Test
    fun `test readEnv with special characters in env name`() {
        val code = "let special: string = readEnv(\"MY_SPECIAL_VAR_123\");"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test long variable names with const`() {
        val code = "const veryLongVariableNameForTesting: string = \"value\";"
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(1, ast.size)
    }

    @Test
    fun `test program mixing old and new features`() {
        val code =
            """
            let counter: number = 0;
            const max: number = 100;
            const user: string = readInput("");
            counter = counter + 1;
            println("Counter: " + counter);
            """.trimIndent()
        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(5, ast.size)
    }

    @Test
    fun `test const without initialization should throw exception`() {
        val code = "const value: number;" // Missing initialization

        assertThrows(Exception::class.java) {
            parseCode(code)
        }
    }

    @Test
    fun `test readEnv without parameter should throw exception`() {
        val code = "let env: string = readEnv();" // Missing parameter

        assertThrows(Exception::class.java) {
            parseCode(code)
        }
    }

    // Additional tests

    @Test
    fun `test readInput creates FunctionToken and FunctionNode`() {
        val code = "let userInput : string = readInput(\"\");"

        val ast = parseCode(code)

        // Verify the AST contains a FunctionNode for readInput
        val declarationNode = ast[0] as DeclaratorNode
        val assignmentExpression = declarationNode.value // Assuming this gets the assigned value
        assertTrue(assignmentExpression is FunctionNode, "readInput should create a FunctionNode")
        assertEquals(FunctionEnum.READ_INPUT, (assignmentExpression).functionName)
    }

    @Test
    fun `test readEnv creates FunctionToken and FunctionNode with parameter`() {
        val code = "let envVar: string = readEnv(\"PATH\");"

        val ast = parseCode(code)

        val declaratorNode = ast[0] as DeclaratorNode
        val assignmentExpression = declaratorNode.value

        assertTrue(assignmentExpression is FunctionNode, "readEnv should create a FunctionNode")
        assertEquals(FunctionEnum.READ_ENV, (assignmentExpression).functionName)
    }

    @Test
    fun `test println function token and node verification`() {
        val code = "println(\"Hello World\");"

        val ast = parseCode(code)

        val functionNode = ast[0] as FunctionNode
        assertEquals(FunctionEnum.PRINTLN, (functionNode).functionName)
    }

//    @Test
//    fun `test function call in expression creates correct nodes`() {
//        val code = "let message: string = \"Hello \" + readInput();"
//
//        val ast = parseCode(code)
//
//        val declarationNode = ast[0] as DeclaratorNode
//        val binaryExpression = declarationNode.value as BinaryOpNode // Assuming + creates BinaryExpressionNode
//
//        assertTrue(binaryExpression.left is LiteralNode)
//        assertEquals("Hello ", (binaryExpression.left as LiteralNode).value.toString())
//
//        assertTrue(binaryExpression.right is FunctionNode)
//        assertEquals(FunctionEnum.READ_INPUT, (binaryExpression.right as FunctionNode).functionName)
//    }

//    fixme
//    @Test
//    fun `test function with complex parameter creates correct tokens and nodes`() {
//        val code = "let value: string = readEnv(\"PREFIX_\" + \"SUFFIX\");"
//
//        val ast = parseCode(code)
//
//        val declaratorNode = ast[0] as DeclaratorNode
//        val functionNode = declaratorNode.value as FunctionNode
//
//        assertEquals(FunctionEnum.READ_ENV, functionNode.functionName)
//        assertEquals(1, functionNode.arguments)
//
//        // Verify the argument is a binary expression (string concatenation)
//        val argument = functionNode.arguments as BinaryOpNode
//        assertTrue(argument.left is LiteralNode)
//        assertTrue(argument.right is LiteralNode)
//        assertEquals("PREFIX_", (argument.left as LiteralNode).value.toString())
//        assertEquals("SUFFIX", (argument.right as LiteralNode).value.toString())
//    }

    @Test
    fun `test multiple function calls create distinct FunctionNodes`() {
        val code =
            """
            let userName: string = readInput("");
            let homePath: string = readEnv("HOME");
            println("Welcome " + userName);
            """.trimIndent()

        val ast = parseCode(code)

        assertNotNull(ast)
        assertEquals(3, ast.size)

        // Verify first statement: readInput
        val firstDecl = ast[0] as DeclaratorNode
        val firstFunction = firstDecl.value as FunctionNode
        assertEquals(FunctionEnum.READ_INPUT, firstFunction.functionName)

        // Verify second statement: readEnv
        val secondDecl = ast[1] as DeclaratorNode
        val secondFunction = secondDecl.value as FunctionNode
        assertEquals(FunctionEnum.READ_ENV, secondFunction.functionName)

        // Verify third statement: println
        val thirdFunction = ast[2] as FunctionNode
        assertEquals(FunctionEnum.PRINTLN, thirdFunction.functionName)
    }

    @Test
    fun `test function token properties`() {
        val code = "readInput(\"\");" // readInput() -> Deberia mandar String vacio como resolucion -> edge case: agregar string y parsearlo

        // Test that parseCode correctly processes function tokens
        val ast = parseCode(code)

        val functionNode = ast[0] as FunctionNode

        // Test function node properties
        assertEquals(FunctionEnum.READ_INPUT, functionNode.functionName)
    }

//    fixme
//    @Test
//    fun `test function node properties after parsing`() {
//        val code = "let result: string = readEnv(\"TEST\");"
//
//        val ast = parseCode(code)
//
//        val declaratorNode = ast[0] as DeclaratorNode
//        val functionNode = declaratorNode.value as FunctionNode
//
//        // Test FunctionNode properties
//        assertEquals(FunctionEnum.READ_ENV, functionNode.functionName)
//        assertEquals(1, functionNode.arguments)
//        assertTrue(functionNode.arguments is LiteralNode)
//        assertEquals("TEST", (functionNode.arguments as LiteralNode).value.toString())
//    }
}
