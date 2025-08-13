package token

import token.abs.TokenInterface

enum class Function {
    PRINTLN
}

class FunctionToken(
    override val value: Function,
    override val row: Int,
    override val position: Int
) : TokenInterface {
    override val name: String = "function";
}
