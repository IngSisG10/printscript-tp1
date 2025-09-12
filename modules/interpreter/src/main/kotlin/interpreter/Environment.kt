package interpreter

import common.enums.DeclarationTypeEnum
import common.enums.TypeEnum
import common.exception.InterpreterException
import common.exception.TypeMismatchException
import common.exception.UndefinedVariableException
import common.exception.UninitializedVariableException

data class Variable(
    val type: TypeEnum,
    val value: Value? = null,
    val declarationType: DeclarationTypeEnum = DeclarationTypeEnum.LET,
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

data class BooleanValue(
    val value: Boolean,
) : Value() {
    override fun toStringValue(): String = value.toString()
}

class Environment {
    private val scopes = mutableListOf(mutableMapOf<String, Variable>())

    fun enterScope() {
        scopes.add(mutableMapOf())
    }

    fun exitScope() {
        if (scopes.size > 1) {
            scopes.removeAt(scopes.lastIndex)
        }
    }

    fun declareVariable(
        name: String,
        type: TypeEnum,
        value: Value? = null,
        declarationType: DeclarationTypeEnum = DeclarationTypeEnum.LET,
    ) {
        val currentScope = scopes.last()
        if (currentScope.containsKey(name)) {
            throw InterpreterException("Variable '$name' is already declared in this scope")
        }
        currentScope[name] = Variable(type, value, declarationType)
    }

    fun setVariable(
        name: String,
        value: Value,
    ) {
        for (scope in scopes.asReversed()) {
            if (scope.containsKey(name)) {
                val variable = scope[name]!!
                if (variable.declarationType == DeclarationTypeEnum.CONST) {
                    throw InterpreterException("Cannot reassign to constant variable '$name'")
                }

                when (variable.type) {
                    TypeEnum.NUMBER ->
                        if (value !is NumberValue) {
                            throw TypeMismatchException(
                                "Cannot assign ${value::class.simpleName} to number variable '$name'",
                            )
                        }
                    TypeEnum.STRING ->
                        if (value !is StringValue) {
                            throw TypeMismatchException(
                                "Cannot assign ${value::class.simpleName} to string variable '$name'",
                            )
                        }
                    TypeEnum.BOOLEAN ->
                        if (value !is BooleanValue) {
                            throw TypeMismatchException(
                                "Cannot assign ${value::class.simpleName} to boolean variable '$name'",
                            )
                        }
                    TypeEnum.ANY -> { /* Any value is fine */ }
                }

                scope[name] = variable.copy(value = value)
                return
            }
        }
        throw UndefinedVariableException(name)
    }

    fun getValue(name: String): Value {
        for (scope in scopes.asReversed()) {
            scope[name]?.let { variable ->
                return variable.value ?: throw UninitializedVariableException(name)
            }
        }
        throw UndefinedVariableException(name)
    }
}
