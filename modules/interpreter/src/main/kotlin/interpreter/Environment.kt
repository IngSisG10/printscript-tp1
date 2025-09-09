package interpreter

import common.exception.InterpreterException
import common.exception.TypeMismatchException
import common.exception.UndefinedVariableException
import common.exception.UninitializedVariableException

data class Variable(
    val type: common.enums.TypeEnum,
    var value: Value? = null,
    val isMutable: Boolean = true,
)

sealed class Value {
    abstract fun toStringValue(): String
}

data class NumberValue(
    val value: Double,
) : Value() {
    override fun toStringValue(): String = value.toString()
}

data class StringValue(
    val value: String,
) : Value() {
    override fun toStringValue(): String = value
}

class Environment {
    // TODO: immutable map
    private val variables = mutableMapOf<String, Variable>()

    fun declareVariable(
        name: String,
        type: common.enums.TypeEnum,
        value: Value? = null,
        isMutable: Boolean = true,
    ) {
        if (variables.containsKey(name)) {
            throw InterpreterException("Variable '$name' is already declared")
        }
        variables[name] = Variable(type, value, isMutable)
    }

    fun setVariable(
        name: String,
        value: Value,
    ) {
        val variable = variables[name] ?: throw UndefinedVariableException(name)

        when {
            variable.type == common.enums.TypeEnum.NUMBER && value !is NumberValue ->
                throw TypeMismatchException("Cannot assign ${value::class.simpleName} to number variable")
            variable.type == common.enums.TypeEnum.STRING && value !is StringValue ->
                throw TypeMismatchException("Cannot assign ${value::class.simpleName} to string variable")
        }

        variable.value = value
    }

    fun getValue(name: String): Value {
        val variable = variables[name] ?: throw UndefinedVariableException(name)
        return variable.value ?: throw UninitializedVariableException(name)
    }
}
