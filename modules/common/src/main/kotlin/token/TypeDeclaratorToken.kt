package token

import token.abs.TokenInterface

class TypeDeclaratorToken(
    override val row: Int,
    override val position: Int
) : TokenInterface {

    override val name: String = "type_declarator"
    override val value: String = ":"
}