package parser

import token.abs.TokenInterface

interface StructureValidator {
    fun validate(line: List<TokenInterface>)
}
