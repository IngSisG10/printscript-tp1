package org.example.token

import org.example.token.abs.TokenInterface

class NumberLiteralToken(
    override val value: Number,
    override val row: Int,
    override val position: Int
) : TokenInterface {
    override val name: String = "number_literal"
}