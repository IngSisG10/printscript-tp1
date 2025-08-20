package token

import enums.TypeEnum
import token.abs.TokenInterface

class TypeToken(
    override val value: TypeEnum,
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "type"
}
