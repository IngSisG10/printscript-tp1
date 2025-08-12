package interpreter

sealed class RuntimeValue {
    abstract val type: String
}

data class NumberValue(val value: Double) : RuntimeValue() {
    override val type = "number"
}

data class StringValue(val value: String) : RuntimeValue() {
    override val type = "string"
}

data class NullValue(val value: String = "null") : RuntimeValue() {
    override val type = "null"
}