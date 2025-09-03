import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import helper.CommandLineInterface
import helper.commands.Analyze
import helper.commands.Execute
import helper.commands.Format

fun main(args: Array<String>): Unit =
    CommandLineInterface()
        .subcommands(
            Execute(),
            Analyze(),
            Format(),
        ).main(args)
