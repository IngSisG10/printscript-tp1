package cli

import cli.helper.commands.Lint
import cli.testutil.Golden
import com.github.ajalt.clikt.testing.test
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals

class LintTests {
    @TempDir
    lateinit var tmp: Path

    @Test
    fun `snake case valid`() {
        // Arrange
        val program =
            """
            let hola_mundo : string = "hola mundo";
            """.trimIndent()
        val file = tmp.resolve("snake_case.ps")
        Files.writeString(file, program)

        val json =
            """
            {
                "identifier_format": "snake case"
            }
            """.trimIndent()
        val jsonFile = tmp.resolve("snake_case.json")
        Files.writeString(jsonFile, json)

        val result = Lint().test(arrayOf(file.toString(), jsonFile.toString(), "--version", "1.0"))

        // Assert
        assertEquals(0, result.statusCode)
        Golden.assertGolden("/golden/snake_case.out", result.stdout)
    }
}
