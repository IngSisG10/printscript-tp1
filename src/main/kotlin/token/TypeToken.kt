package org.example.token

import org.example.token.abs.TokenInterface

enum class Type {
    STRING,
    BOOLEAN,
    NUMBER,
    ANY
}

class TypeToken(
    override val value: Type,
    override val row: Int,
    override val position: Int
) : TokenInterface {
    override val name: String = "type";
}