package parser.nodecreator.validators.abs

import common.token.abs.TokenInterface

interface StructureValidator {
    fun validate(line: List<TokenInterface>)
}
