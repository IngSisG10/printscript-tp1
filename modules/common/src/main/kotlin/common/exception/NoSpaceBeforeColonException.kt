package common.exception

class NoSpaceBeforeColonException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No space before colon", cause)
}
