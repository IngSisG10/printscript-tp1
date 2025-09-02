package integration

import ast.abs.AstInterface
import interpreter.PrintScriptInterpreter
import lexer.Lexer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import parser.Parser

class PrintScriptIntegrationTest {
    private fun executeCode(code: String): List<String> {
        val tokens = Lexer(code).lex()
        val ast = Parser(tokens).parse()
        return PrintScriptInterpreter().interpret(ast as List<AstInterface>)
    }

    @Test
    fun testSimpleVariableDeclarationAndPrint() {
        val code = "let x: Number = 42; println(x);"
        val result = executeCode(code)
        assertEquals(listOf("42.0"), result)
    }

    @Test
    fun testStringVariableDeclarationAndPrint() {
        val code = "let message: String = \"Hello World\"; println(message);"
        val result = executeCode(code)
        assertEquals(listOf("Hello World"), result)
    }

    @Test
    fun testArithmeticExpression() {
        val code = "let result: Number = 5 + 3 * 2; println(result);"
        val result = executeCode(code)
        assertEquals(listOf("11.0"), result)
    }

    @Test
    fun testVariableAssignment() {
        val code =
            """
            let x: Number = 10;
            println(x);
            x = 20;
            println(x);
            """.trimIndent()
        val result = executeCode(code)
        assertEquals(listOf("10.0", "20.0"), result)
    }

    /*@Test
    fun testStringConcatenation() {
        val code =
            """
            let greeting: String = "Hello";
            let name: String = "World";
            let message: String = greeting + " " + name;
            println(message);
            """.trimIndent()
        val result = executeCode(code)
        assertEquals(listOf("Hello World"), result)
    }*/

    @Test
    fun testStringAndNumberConcatenation() {
        val code =
            """
            let label: String = "Value: ";
            let value: Number = 42;
            println(label + value);
            """.trimIndent()
        val result = executeCode(code)
        assertEquals(listOf("Value: 42.0"), result)
    }

   /* @Test
    fun testComplexArithmeticExpression() {
        val code =
            """
            let a: Number = 10;
            let b: Number = 5;
            let c: Number = 2;
            let result: Number = a + b * c - 8 / 4;
            println(result);
            """.trimIndent()
        val result = executeCode(code)
        assertEquals(listOf("18.0"), result)
    }*/

    @Test
    fun testMultipleVariableDeclarations() {
        val code =
            """
            let x: Number = 1;
            let y: Number = 2;
            let z: Number = 3;
            println(x + y + z);
            """.trimIndent()
        val result = executeCode(code)
        assertEquals(listOf("6.0"), result)
    }

    @Test
    fun testVariableReuse() {
        val code =
            """
            let counter: Number = 0;
            println(counter);
            counter = counter + 1;
            println(counter);
            counter = counter * 5;
            println(counter);
            """.trimIndent()
        val result = executeCode(code)
        assertEquals(listOf("0.0", "1.0", "5.0"), result)
    }

    /*@Test
    fun testDivisionOperation() {
        val code =
            """
            let dividend: Number = 15;
            let divisor: Number = 3;
            let quotient: Number = dividend / divisor;
            println(quotient);
            """.trimIndent()
        val result = executeCode(code)
        assertEquals(listOf("5.0"), result)
    }

    @Test
    fun testSubtractionOperation() {
        val code =
            """
            let minuend: Number = 20;
            let subtrahend: Number = 8;
            let difference: Number = minuend - subtrahend;
            println(difference);
            """.trimIndent()
        val result = executeCode(code)
        assertEquals(listOf("12.0"), result)
    }

    @Test
    fun testMultiplicationOperation() {
        val code =
            """
            let factor1: Number = 6;
            let factor2: Number = 7;
            let product: Number = factor1 * factor2;
            println(product);
            """.trimIndent()
        val result = executeCode(code)
        assertEquals(listOf("42.0"), result)
    }*/

    @Test
    fun testEmptyStringHandling() {
        val code =
            """
            let empty: String = "";
            let filled: String = "content";
            println(empty + filled);
            """.trimIndent()
        val result = executeCode(code)
        assertEquals(listOf("content"), result)
    }

    @Test
    fun testAnyTypeWithNumber() {
        val code =
            """
            let value: Any = 42;
            println(value);
            """.trimIndent()
        val result = executeCode(code)
        assertEquals(listOf("42.0"), result)
    }

    @Test
    fun testAnyTypeWithString() {
        val code =
            """
            let value: Any = "hello";
            println(value);
            """.trimIndent()
        val result = executeCode(code)
        assertEquals(listOf("hello"), result)
    }

    /*@Test
    fun testComplexNestedExpression() {
        val code =
            """
            let a: Number = 2;
            let b: Number = 3;
            let c: Number = 4;
            println((a + b) * c - 5);
            """.trimIndent()
        val result = executeCode(code)
        assertEquals(listOf("15.0"), result)
    }*/

    @Test
    fun testPrintlnWithDirectLiteral() {
        val code =
            """
            println(100);
            println("Direct string");
            """.trimIndent()
        val result = executeCode(code)
        assertEquals(listOf("100.0", "Direct string"), result)
    }

    @Test
    fun testPrintlnWithDirectExpression() {
        val code =
            """
            println(5 + 5);
            println("Hello" + " " + "World");
            """.trimIndent()
        val result = executeCode(code)
        assertEquals(listOf("10.0", "Hello World"), result)
    }

    // Error handling tests
    @Test
    fun testDivisionByZeroError() {
        val code = "println(10 / 0);"
        assertThrows(Exception::class.java) {
            executeCode(code)
        }
    }

    @Test
    fun testUndefinedVariableError() {
        val code = "println(undefinedVariable);"
        assertThrows(Exception::class.java) {
            executeCode(code)
        }
    }

    /*@Test
    fun testTypeMismatchError() {
        val code =
            """
            let x: Number = 5;
            x = "not a number";
            """.trimIndent()
        assertThrows(Exception::class.java) {
            executeCode(code)
        }
    }*/

    @Test
    fun testVariableRedeclarationError() {
        val code =
            """
            let x: Number = 5;
            let x: String = "duplicate";
            """.trimIndent()
        assertThrows(Exception::class.java) {
            executeCode(code)
        }
    }

    @Test
    fun testInvalidArithmeticOperationError() {
        val code =
            """
            let str1: String = "hello";
            let str2: String = "world";
            println(str1 - str2);
            """.trimIndent()
        assertThrows(Exception::class.java) {
            executeCode(code)
        }
    }
}
