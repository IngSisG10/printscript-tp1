import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import helper.CommandLineInterface
import helper.commands.Analyze
import helper.commands.Execute

fun main(args: Array<String>): Unit =
    CommandLineInterface()
        .subcommands(
            Execute(),
            Analyze(),
        ).main(args)
