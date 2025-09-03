package common.token

import common.token.abs.TokenInterface

class ElseToken(
    override val row: Int,
    override val position: Int,
) : common.token.abs.TokenInterface {
    override val name: String = "else"
    override val value: String = "else"
}
