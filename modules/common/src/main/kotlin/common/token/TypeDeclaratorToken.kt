package common.token

import common.token.abs.TokenInterface

class TypeDeclaratorToken(
    override val row: Int,
    override val position: Int,
) : common.token.abs.TokenInterface {
    override val name: String = "type_declarator"
    override val value: String = ":"
}
