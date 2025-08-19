package interpreter

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
            throw RuntimeException("Variable '$name' is already declared")
        }
        variables[name] = Variable(type, value)
    }

    fun getVariable(name: String): Variable {
        return variables[name] ?: throw RuntimeException("Variable '$name' doesn't exist")
    }

    fun setVariable(name: String, value: Value) {
        val variable = variables[name] ?: throw RuntimeException("Variable '$name' is not declared")

        when {
            variable.type == Type.NUMBER && value !is NumberValue ->
                throw RuntimeException("Variable '$name' is an int")
            variable.type == Type.STRING && value !is StringValue ->
                throw RuntimeException("Variable '$name' is a string")
        }

        variable.value = value
    }

    fun getValue(name: String): Value {
        val variable = getVariable(name)
        return variable.value ?: throw RuntimeException("Variable '$name' is not declared")
    }
}