package interpreter

import common.exception.InterpreterException
import common.exception.TypeMismatchException
import common.exception.UndefinedVariableException
import common.exception.UninitializedVariableException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EnvironmentTest {
    private lateinit var environment: Environment

    @BeforeEach
    fun setUp() {
        environment = Environment()
    }

    @Test
    fun testDeclareAndGetNumberVariable() {
        val value = NumberValue(42.0)
        environment.declareVariable("x", common.enums.TypeEnum.NUMBER, value)

        val retrievedValue = environment.getValue("x")
        assertEquals(value, retrievedValue)
    }

    @Test
    fun testDeclareAndGetStringVariable() {
        val value = StringValue("Hello, World!")
        environment.declareVariable("message", common.enums.TypeEnum.STRING, value)

        val retrievedValue = environment.getValue("message")
        assertEquals(value, retrievedValue)
    }

    @Test
    fun testDeclareVariableWithoutInitialValue() {
        environment.declareVariable("x", common.enums.TypeEnum.NUMBER)

        assertThrows(UninitializedVariableException::class.java) {
            environment.getValue("x")
        }
    }

    @Test
    fun testSetVariableAfterDeclaration() {
        environment.declareVariable("x", common.enums.TypeEnum.NUMBER)
        val value = NumberValue(10.0)
        environment.setVariable("x", value)

        val retrievedValue = environment.getValue("x")
        assertEquals(value, retrievedValue)
    }

    @Test
    fun testVariableRedeclarationThrowsException() {
        environment.declareVariable("x", common.enums.TypeEnum.NUMBER, NumberValue(5.0))

        assertThrows(InterpreterException::class.java) {
            environment.declareVariable("x", common.enums.TypeEnum.STRING, StringValue("hello"))
        }
    }

    @Test
    fun testGetUndefinedVariableThrowsException() {
        assertThrows(UndefinedVariableException::class.java) {
            environment.getValue("undefined")
        }
    }

    @Test
    fun testSetUndefinedVariableThrowsException() {
        assertThrows(UndefinedVariableException::class.java) {
            environment.setVariable("undefined", NumberValue(42.0))
        }
    }

    @Test
    fun testSetVariableWithWrongTypeThrowsException() {
        environment.declareVariable("x", common.enums.TypeEnum.NUMBER, NumberValue(10.0))

        assertThrows(TypeMismatchException::class.java) {
            environment.setVariable("x", StringValue("not a number"))
        }
    }

    @Test
    fun testSetStringVariableWithNumberThrowsException() {
        environment.declareVariable("message", common.enums.TypeEnum.STRING, StringValue("hello"))

        assertThrows(TypeMismatchException::class.java) {
            environment.setVariable("message", NumberValue(42.0))
        }
    }

    @Test
    fun testAnyTypeAcceptsAnyValue() {
        environment.declareVariable("x", common.enums.TypeEnum.ANY)

        environment.setVariable("x", NumberValue(42.0))
        assertEquals(NumberValue(42.0), environment.getValue("x"))

        environment.setVariable("x", StringValue("hello"))
        assertEquals(StringValue("hello"), environment.getValue("x"))
    }

    @Test
    fun testMultipleVariables() {
        environment.declareVariable("x", common.enums.TypeEnum.NUMBER, NumberValue(10.0))
        environment.declareVariable("y", common.enums.TypeEnum.STRING, StringValue("hello"))
        environment.declareVariable("z", common.enums.TypeEnum.ANY, NumberValue(20.0))

        assertEquals(NumberValue(10.0), environment.getValue("x"))
        assertEquals(StringValue("hello"), environment.getValue("y"))
        assertEquals(NumberValue(20.0), environment.getValue("z"))
    }

    @Test
    fun testVariableUpdate() {
        environment.declareVariable("counter", common.enums.TypeEnum.NUMBER, NumberValue(0.0))
        assertEquals(NumberValue(0.0), environment.getValue("counter"))

        environment.setVariable("counter", NumberValue(1.0))
        assertEquals(NumberValue(1.0), environment.getValue("counter"))

        environment.setVariable("counter", NumberValue(2.0))
        assertEquals(NumberValue(2.0), environment.getValue("counter"))
    }
}
