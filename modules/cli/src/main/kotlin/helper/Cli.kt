package helper

import helper.rules.BuildFile

class Cli {
    val rules =
        listOf(
            BuildFile(),
        )

    fun execute(args: Array<String>) {
        for (rule in rules) {
            rule.tryRun(args)
        }
    }
}
