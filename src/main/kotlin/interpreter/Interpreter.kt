package interpreter

import org.example.parser.Node
import java.util.*

interface Interpreter {

    fun interpret(astList: List<Optional<Node>>)

}