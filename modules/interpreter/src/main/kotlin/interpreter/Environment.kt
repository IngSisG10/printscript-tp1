package interpreter

import common.enums.DeclarationTypeEnum
import common.enums.TypeEnum
import common.exception.InterpreterException
import common.exception.TypeMismatchException
import common.exception.UndefinedVariableException
import common.exception.UninitializedVariableException

sealed interface VariableState {
    val type: TypeEnum
    val declarationType: DeclarationTypeEnum
}

data class Initialized(
    override val type: TypeEnum,
    override val declarationType: DeclarationTypeEnum,
    val value: Value,
) : VariableState

data class Uninitialized(
    override val type: TypeEnum,
    override val declarationType: DeclarationTypeEnum,
) : VariableState

sealed class Value {
    abstract fun toStringValue(): String
}

data class NumberValue(
    val value: Double,
) : Value() {
    override fun toStringValue(): String =
        if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            value.toString()
        }
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
    private val scopes = mutableListOf(mutableMapOf<String, VariableState>())

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

        val variableState =
            if (value != null) {
                Initialized(type, declarationType, value)
            } else {
                if (declarationType == DeclarationTypeEnum.CONST) {
                    throw InterpreterException("Constant variable '$name' must be initialized")
                }
                Uninitialized(type, declarationType)
            }
        currentScope[name] = variableState
    }

    fun setVariable(
        name: String,
        value: Value,
    ) {
        for (scope in scopes.asReversed()) {
            if (scope.containsKey(name)) {
                val variableState = scope[name]!!
                if (variableState.declarationType == DeclarationTypeEnum.CONST) {
                    throw InterpreterException("Cannot reassign to constant variable '$name'")
                }

                when (variableState.type) {
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

                scope[name] = Initialized(variableState.type, variableState.declarationType, value)
                return
            }
        }
        throw UndefinedVariableException(name)
    }

    fun getValue(name: String): Value {
        for (scope in scopes.asReversed()) {
            scope[name]?.let { variableState ->
                return when (variableState) {
                    is Initialized -> variableState.value
                    is Uninitialized -> throw UninitializedVariableException(name)
                }
            }
        }
        throw UndefinedVariableException(name)
    }
}
