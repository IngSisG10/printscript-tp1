package cli

import cli.helper.CommandLineInterface
import cli.helper.commands.Analyze
import cli.helper.commands.Execute
import cli.helper.commands.Format
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>): Unit =
    CommandLineInterface()
        .subcommands(
            Execute(),
            Analyze(),
            Format(),
        ).main(args)
