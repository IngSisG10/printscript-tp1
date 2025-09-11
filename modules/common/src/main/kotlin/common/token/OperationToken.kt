package common.token

import common.enums.OperationEnum
import common.token.abs.OperationInterface
import common.token.abs.TokenInterface

class OperationToken(
    override val value: OperationEnum,
    override val row: Int,
    override val position: Int,
) : TokenInterface,
    OperationInterface {
    override val name: String = "operation"
    override val priority: Int = evaluatePriority()

    private fun evaluatePriority(): Int =
        when (value) {
            OperationEnum.SUM -> 8
            OperationEnum.MINUS -> 7
            OperationEnum.MULTIPLY -> 6
            OperationEnum.DIVIDE -> 5
            else -> 0
        }
}
