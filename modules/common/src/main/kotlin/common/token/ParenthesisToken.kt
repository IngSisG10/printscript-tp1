package common.token

import common.token.abs.TokenInterface

// TODO: Delete obsolete class
class ParenthesisToken(
    override val value: List<common.token.abs.TokenInterface>,
    override val row: Int,
    override val position: Int,
    val closePosition: Int,
) : common.token.abs.TokenInterface {
    override val name: String = "parenthesis"
}

data class ParenthesisData(
    val parenthesisData: String,
    val endParenthesis: Int,
    val rowDelta: Int = 0,
)
