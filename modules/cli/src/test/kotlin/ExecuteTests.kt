package cli

import cli.helper.commands.Execute
import cli.testutil.Golden
import com.github.ajalt.clikt.testing.test
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class ExecuteTests {
    @TempDir
    lateinit var tmp: Path

    @Test
    fun `hello world`() {
        // Arrange: programa de ejemplo
        val program =
            """
            println("Hello");
            println("World");
            """.trimIndent()
        val file = tmp.resolve("hello.ps")
        Files.writeString(file, program)

        // Act
        val result = Execute().test(arrayOf(file.toString(), "--version", "1.0"))

        // Assert exit code (si tu comando devuelve 0 en éxito)
        assert(result.statusCode == 0)

        // Golden
        Golden.assertGolden("/golden/hello.out", result.stdout)
    }

    @Test
    fun `archivo inexistente`() {
        val result = Execute().test(arrayOf("no_existe.ps"))
        // exit code no-cero esperado
        assert(result.statusCode != 0)
        Golden.assertGolden("/golden/error_missing_file.out", (result.stdout + result.stderr))
    }

    @Test
    fun `operaciones combinadas`() {
        val program =
            """
            let a : number = 5 + 5;
            let b : number = a * 5 + 400 / 4 - 1;
            println(b);
            """.trimIndent()
        val file = tmp.resolve("combined_operations.ps")
        Files.writeString(file, program)
        val result = Execute().test(arrayOf(file.toString(), "--version", "1.0"))
        assert(result.statusCode == 0)
        Golden.assertGolden("/golden/combined_operations.out", result.stdout)
    }
}
