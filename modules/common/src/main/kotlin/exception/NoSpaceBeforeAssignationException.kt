package exception

class NoSpaceBeforeAssignationException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No space before assignation operator '='", cause)
}
