package common.token

import common.token.abs.TokenInterface

class ConstantDeclaratorToken(
    override val row: Int,
    override val position: Int,
) : common.token.abs.TokenInterface {
    override val name: String = "constant"
    override val value: String = "const"
}
