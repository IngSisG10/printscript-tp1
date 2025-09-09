package parser.nodecreator.validators

import common.token.abs.TokenInterface

interface StructureValidator {
    fun validate(line: List<TokenInterface>)
}
