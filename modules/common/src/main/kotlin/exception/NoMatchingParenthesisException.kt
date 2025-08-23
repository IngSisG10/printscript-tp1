package exception

public class NoMatchingParenthesisException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    constructor(cause: Throwable? = null) : this("No matching parenthesis found", cause)
}
