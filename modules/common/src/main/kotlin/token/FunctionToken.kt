package token

import enums.FunctionEnum
import token.abs.TokenInterface

class FunctionToken(
    override val value: FunctionEnum,
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "function"
}
