package common.token

import common.token.abs.TokenInterface

class CloseBraceToken(
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "close_brace"
    override val value: String = "}"
}
