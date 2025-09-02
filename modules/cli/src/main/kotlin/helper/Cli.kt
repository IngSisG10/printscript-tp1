package helper

import helper.rules.ExecutionCommand

class Cli {
    val rules =
        listOf(
            ExecutionCommand(),
        )

    fun execute(args: Array<String>) {
        for (rule in rules) {
            rule.tryRun(args)
        }
    }
}
