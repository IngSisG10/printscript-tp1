package token

import enums.OperationEnum
import token.abs.TokenInterface

class OperationToken(
    override val value: OperationEnum,
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "operation"
}
