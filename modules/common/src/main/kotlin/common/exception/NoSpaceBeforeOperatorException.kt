package exception

import exception.NoSpaceAfterOperatorException

class NoSpaceBeforeOperatorException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No space before operator", cause)
}
