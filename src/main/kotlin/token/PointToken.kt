package org.example.token

import org.example.token.abs.TokenInterface

class PointToken(
    override val row: Int,
    override val position: Int
) : TokenInterface {
    override val name: String = "point"
    override val value: String = "."
}