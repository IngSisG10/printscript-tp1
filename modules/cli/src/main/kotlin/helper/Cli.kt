package helper

import helper.commands.AnalyzingCommand
import helper.commands.ExecutionCommand

class Cli {
    val rules =
        listOf(
            ExecutionCommand(),
            AnalyzingCommand(),
        )

    fun execute(args: Array<String>) {
        for (rule in rules) {
            rule.tryRun(args)
        }
    }
}
