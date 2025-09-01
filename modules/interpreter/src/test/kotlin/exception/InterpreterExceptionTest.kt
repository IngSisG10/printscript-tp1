package exception

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InterpreterExceptionTest {
    @Test
    fun testInterpreterExceptionMessage() {
        val exception = InterpreterException("Test message")
        assertEquals("Test message", exception.message)
    }

    @Test
    fun testUndefinedVariableExceptionMessage() {
        val exception = UndefinedVariableException("myVariable")
        assertEquals("Undefined variable: myVariable", exception.message)
    }

    @Test
    fun testTypeMismatchExceptionMessage() {
        val exception = TypeMismatchException("Cannot assign string to number")
        assertEquals("Type mismatch: Cannot assign string to number", exception.message)
    }

    @Test
    fun testDivisionByZeroExceptionMessage() {
        val exception = DivisionByZeroException()
        assertEquals("Division by zero", exception.message)
    }

    @Test
    fun testUninitializedVariableExceptionMessage() {
        val exception = UninitializedVariableException("myVar")
        assertEquals("Variable 'myVar' is used before being initialized", exception.message)
    }

    @Test
    fun testExceptionInheritance() {
        val undefinedException = UndefinedVariableException("test")
        val typeMismatchException = TypeMismatchException("test")
        val divisionException = DivisionByZeroException()
        val uninitializedException = UninitializedVariableException("test")

        assert(undefinedException is InterpreterException)
        assert(typeMismatchException is InterpreterException)
        assert(divisionException is InterpreterException)
        assert(uninitializedException is InterpreterException)
    }
}
