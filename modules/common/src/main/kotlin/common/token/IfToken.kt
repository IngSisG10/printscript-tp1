package common.token

import common.token.abs.TokenInterface

class IfToken(
    override val row: Int,
    override val position: Int,
) : common.token.abs.TokenInterface {
    override val name: String = "if"
    override val value: String = "if"
}
