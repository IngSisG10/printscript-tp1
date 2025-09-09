package cli

import cli.helper.CommandLineInterface
import cli.helper.commands.Execute
import cli.helper.commands.Format
import cli.helper.commands.Lint
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>): Unit =
    CommandLineInterface()
        .subcommands(
            Execute(),
            Lint(),
            Format(),
        ).main(args)
