package cli

import cli.helper.commands.Format
import cli.testutil.Golden
import com.github.ajalt.clikt.testing.test
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals

class FormatTests {
    @TempDir
    lateinit var tmp: Path

    @Test
    fun `if brace same line`() {
        // Arrange
        val program =
            """
            if (a) 
            {
                println(a);
            }
            """.trimIndent()
        val file = tmp.resolve("if_brace_same_line.ps")
        Files.writeString(file, program)

        val json =
            """
            {
                "if-brace-same-line": true
            }
            """.trimIndent()
        val jsonFile = tmp.resolve("if_brace_same_line.json")
        Files.writeString(jsonFile, json)

        val result = Format().test(arrayOf(file.toString(), jsonFile.toString(), "--version", "1.1"))

        // Assert
        assertEquals(0, result.statusCode)
        Golden.assertGolden("/golden/if_brace_same_line.out", result.stdout)
    }
}
