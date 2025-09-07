package common.token

import common.enums.FunctionEnum
import common.token.abs.TokenInterface

class FunctionToken(
    override val value: FunctionEnum,
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "function"
}
