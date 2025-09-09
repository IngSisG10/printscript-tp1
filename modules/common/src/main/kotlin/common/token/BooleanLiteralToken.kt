package common.token

import common.token.abs.TokenInterface

class BooleanLiteralToken(
    override val value: Boolean,
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "boolean_literal"
}
