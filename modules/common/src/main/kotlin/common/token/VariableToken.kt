package common.token

import common.token.abs.TokenInterface

class VariableToken(
    override val value: String,
    override val row: Int,
    override val position: Int,
) : common.token.abs.TokenInterface {
    override val name: String = "variable"
}
