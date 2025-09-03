package common.exception

open class InterpreterException(
    message: String,
) : Exception(
        message,
    )

class UndefinedVariableException(
    identifier: String,
) : InterpreterException("Undefined variable: $identifier")

class TypeMismatchException(
    message: String,
) : InterpreterException("Type mismatch: $message")

class DivisionByZeroException : InterpreterException("Division by zero")

class UninitializedVariableException(
    identifier: String,
) : InterpreterException("Variable '$identifier' is used before being initialized")
