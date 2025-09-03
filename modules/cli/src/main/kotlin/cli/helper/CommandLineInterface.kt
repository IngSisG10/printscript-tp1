package cli.helper

import com.github.ajalt.clikt.core.CliktCommand

class CommandLineInterface : CliktCommand(name = "cli") {
    override fun run() = Unit
}
