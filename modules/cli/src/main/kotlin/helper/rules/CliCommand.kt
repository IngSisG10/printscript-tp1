package helper.rules

interface CliCommand {
    fun tryRun(args: Array<String>)
}
