package interpreter

import parser.Node
import java.util.*

interface Interpreter {

    fun interpret(astList: List<Optional<Node>>)

}