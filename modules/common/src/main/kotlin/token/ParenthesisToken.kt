package token

import token.abs.TokenInterface

class ParenthesisToken(
    override val value: List<TokenInterface>,
    override val row: Int,
    override val position: Int,
    val closePosition: Int,
) : TokenInterface {
    override val name: String = "parenthesis"
}

data class ParenthesisData(
    val parenthesisData: String,
    val endParenthesis: Int,
)
