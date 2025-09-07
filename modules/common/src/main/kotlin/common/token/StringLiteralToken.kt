package common.token

import common.token.abs.TokenInterface

class StringLiteralToken(
    override val value: String,
    override val row: Int,
    override val position: Int, // position que tiene en la linea
) : TokenInterface {
    override val name: String = "string_literal"
}
