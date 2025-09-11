package lexer.token.rules.util

class RegexUtil {
    companion object {
        fun buildRegex(word: String): Regex = Regex("${Regex.escape(word)}(?![A-Za-z0-9_])")
    }
}
