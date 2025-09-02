package exception

import exception.UnrecognizedLineException

class NoNewLineAfterSemiColon(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No new line after Semi Colon", cause)
}
