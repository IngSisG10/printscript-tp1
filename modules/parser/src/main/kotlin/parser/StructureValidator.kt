package parser

import common.token.abs.TokenInterface

interface StructureValidator {
    fun validate(line: List<common.token.abs.TokenInterface>)
}
