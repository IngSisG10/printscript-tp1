package org.example.parser

import token.abs.TokenInterface
import java.util.*


class Node(
    val token: TokenInterface,
    val left: Optional<Node>,
    val right: Optional<Node>
) {
    fun prettyPrint(prefix: String = "", isTail: Boolean = true) {
        println(prefix + (if (isTail) "└── " else "├── ") + tokenToString(token))
        left.ifPresent {
            it.prettyPrint(prefix + if (isTail) "    " else "│   ", right.isEmpty)
        }
        right.ifPresent {
            it.prettyPrint(prefix + if (isTail) "    " else "│   ", true)
        }
    }

    private fun tokenToString(token: TokenInterface): String {
        return "${token.name}(${token.value})"
    }
}

