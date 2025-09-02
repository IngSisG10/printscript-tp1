package helper.commands

import java.io.File

interface CliCommand {
    fun tryRun(args: Array<String>)

    fun tryFindFile(filename: String): String? {
        val file = File(filename)

        if (!file.exists()) {
            println("file '$filename' not found")
            return null
        }

        return file.readText()
    }
}
