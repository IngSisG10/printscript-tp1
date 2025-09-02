package helper.commands

interface CliCommand {
    fun tryRun(args: Array<String>)
}
