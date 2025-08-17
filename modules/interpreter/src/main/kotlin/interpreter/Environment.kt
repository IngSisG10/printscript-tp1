package interpreter

import exception.InterpreterException
import exception.TypeMismatchException
import exception.UndefinedVariableException
import exception.UninitializedVariableException

enum class Type {
    NUMBER, STRING
}

data class Variable(
    val type: Type,
    var value: Value? = null
)

sealed class Value {
    abstract fun toStringValue(): String
}
data class NumberValue(val value: Double) : Value() {
    override fun toStringValue(): String = value.toString()
}
data class StringValue(val value: String) : Value() {
    override fun toStringValue(): String = value
}

class Environment {
    private val variables = mutableMapOf<String, Variable>()

    fun declareVariable(name: String, type: Type, value: Value? = null) {
        if (variables.containsKey(name)) {
            throw InterpreterException("Variable '$name' is already declared")
        }
        variables[name] = Variable(type, value)
    }

    fun setVariable(name: String, value: Value) {
        val variable = variables[name] ?: throw UndefinedVariableException(name)

        when {
            variable.type == Type.NUMBER && value !is NumberValue ->
                throw TypeMismatchException("Cannot assign ${value::class.simpleName} to number variable")
            variable.type == Type.STRING && value !is StringValue ->
                throw TypeMismatchException("Cannot assign ${value::class.simpleName} to string variable")
        }

        variable.value = value
    }

    fun getValue(name: String): Value {
        val variable = variables[name] ?: throw UndefinedVariableException(name)
        return variable.value ?: throw UninitializedVariableException(name)
    }
}