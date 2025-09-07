package common.token

import common.token.abs.TokenInterface

// separa lineas
class EndSentenceToken(
    override val row: Int,
    override val position: Int,
) : TokenInterface {
    override val name: String = "end_sentence"
    override val value: String = ";"
}
