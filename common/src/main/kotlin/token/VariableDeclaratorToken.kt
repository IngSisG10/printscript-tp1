package token

import token.abs.TokenInterface


class VariableDeclaratorToken(
    override val row: Int,
    override val position: Int
) : TokenInterface {

    override val name: String = "variable_declarator";
    override val value: String = "let";
}

