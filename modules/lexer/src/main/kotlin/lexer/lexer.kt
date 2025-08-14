package lexer

import token.*
import token.Function
import token.abs.TokenInterface

class Lexer(
    private val code: String
) {

    private val listOfTokens = mutableListOf<TokenInterface>()

    private val stringRegex = Regex("\"(.*?)\"") // Finds text inside " "

    private val numberRegex = Regex("\\d+") // Finds int numbers (only positive)

    private var row = 0

    fun lex(): List<TokenInterface> {
        // val listOfLines: List<String> = this.splitIntoLines(code)
        tokenizeLine(code)
        return listOfTokens
    }

    // todo: a futuro, quiza debamos implementar el Visitor pattern
    private fun tokenizeLine(line: String) {
        var i = 0
        val length = line.length

        while (i < length) {
            val c = line[i]

            // todo: a futuro -> Map
            when {

                c == '\n' -> {
                    println("row changes")
                    row++
                    i++  // avanzamos 1
                }

                c.isWhitespace() -> { // " "
                    i++
                }

                this.consumeStringToken(line, row, i)?.also { i = it } != null -> {}

                this.consumeNumberToken(line, row, i)?.also { i = it } != null -> {}

                line.startsWith("println", i) -> {
                    listOfTokens.add(FunctionToken(Function.PRINTLN, row, i))
                    i += 7
                }

                line.startsWith("let", i) -> {
                    listOfTokens.add(VariableDeclaratorToken(row, i))
                    i += 3
                }

                line.startsWith("String", i) -> {
                    listOfTokens.add(TypeToken(Type.STRING, row, i))
                    i += 6
                }

                line.startsWith("Number", i) -> {
                    listOfTokens.add(TypeToken(Type.NUMBER, row, i))
                    i += 6
                }

                line.startsWith("Boolean", i) -> {
                    listOfTokens.add(TypeToken(Type.BOOLEAN, row, i))
                    i += 7
                }

                line.startsWith("Any", i) -> {
                    listOfTokens.add(TypeToken(Type.ANY, row, i))
                    i += 3
                }

                c == '+' -> {
                    listOfTokens.add(OperationToken(Operation.SUM, row, i))
                    i++
                }

                c == '-' -> {
                    listOfTokens.add(OperationToken(Operation.MINUS, row, i))
                    i++
                }

                c == '*' -> {
                    listOfTokens.add(OperationToken(Operation.MULTIPLY, row, i))
                    i++
                }

                c == '/' -> {
                    listOfTokens.add(OperationToken(Operation.DIVIDE, row, i))
                    i++
                }

                c == '=' -> {
                    listOfTokens.add(OperationToken(Operation.EQUAL, row, i))
                    i++
                }

                c == ':' -> {
                    listOfTokens.add(TypeDeclaratorToken(row, i))
                    i++
                }

                c == '.' -> {
                    listOfTokens.add(PointToken(row, i))
                    i++
                }

                c == ';' -> {
                    listOfTokens.add(EndSentenceToken(row, i))
                    i++
                }

                c == '(' -> {
                    val p = extractFirstParenthesisWithNesting(line, i)
                    if (p == null) {
                        // Manejo de error: paréntesis sin cerrar
                        i++
                    } else {
                        // Lexear adentro con un lexer nuevo para evitar estado compartido
                        val innerTokens = Lexer(p.parenthesisData).lex()

                        // Crear tu token de paréntesis agrupado
                        listOfTokens.add(
                            ParenthesisToken(
                                value = innerTokens,
                                row = row,
                                position = i,          // posición del '('
                                closePosition = p.endParenthesis // índice del ')' en 'line'
                            )
                        )

                        // Saltar a justo después del ')'
                        i = p.endParenthesis + 1
                    }
                }

                c == ')' -> {
                    // Manejo de error: paréntesis mal puesto
                    i++
                }

                else -> {
                    // Detectar variables (identificadores)
                    val start = i
                    while (i < length && line[i].isLetterOrDigit()) {
                        i++
                    }
                    val text = line.substring(start, i)
                    if (text.isNotBlank()) {
                        listOfTokens.add(VariableToken(text, row, start))
                    }
                }
            }
        }
    }

    private fun consumeStringToken(line: String, row: Int, i: Int): Int? {
        val match = stringRegex.find(line, i)?.takeIf { it.range.first == i } ?: return null
        val content = match.groupValues[1]
        listOfTokens.add(StringLiteralToken(content, row, i))
        return match.range.last + 1
    }

    private fun consumeNumberToken(line: String, row: Int, i: Int): Int? {
        val match = numberRegex.find(line, i)?.takeIf { it.range.first == i } ?: return null
        val content = match.value
        listOfTokens.add(NumberLiteralToken(content.toInt(), row, i))
        return match.range.last + 1
    }

    private fun extractFirstParenthesisWithNesting(s: String, idx: Int): ParenthesisData? {
        var depth = 0
        var start = -1
        for (i in idx..<s.length) {
            val ch = s[i]
            if (ch == '(') {
                if (depth == 0) start = i
                depth++
            } else if (ch == '\n') {
                row++
            } else if (ch == ')') {
                depth--
                if (depth == 0 && start != -1) {
                    return ParenthesisData(
                        parenthesisData = s.substring(start + 1, i),
                        endParenthesis = i
                    )
                }
            }
        }
        return null // sin cerrar
    }
}
