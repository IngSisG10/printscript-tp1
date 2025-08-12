package org.example.token

import token.abs.TokenInterface

enum class Operation {
    SUM,
    MINUS,
    MULTIPLY,
    DIVIDE,
    EQUAL
}

class OperationToken(
    override val value: Operation,
    override val row: Int,
    override val position: Int
) : TokenInterface {
    override val name: String = "operation"
}

