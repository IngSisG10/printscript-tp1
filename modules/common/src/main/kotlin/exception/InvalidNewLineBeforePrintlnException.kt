package exception

class InvalidNewLineBeforePrintlnException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(expected: Int, found: Int, cause: Throwable? = null) : this(
        "Invalid number of new lines before 'println'. Expected: $expected, Found: $found",
        cause,
    )
}
