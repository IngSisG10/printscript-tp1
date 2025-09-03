package common.token

import common.token.abs.TokenInterface

class VariableDeclaratorToken(
    override val row: Int,
    override val position: Int,
) : common.token.abs.TokenInterface {
    override val name: String = "variable_declarator"
    override val value: String = "let"
}
