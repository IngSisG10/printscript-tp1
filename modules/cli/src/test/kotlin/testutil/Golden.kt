package cli.testutil

import java.io.File
import java.nio.charset.StandardCharsets
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText
import kotlin.test.assertEquals

object Golden {
    private fun normalize(s: String) = s.replace("\r\n", "\n").trimEnd()

    /**
     * Compara la salida actual con un archivo golden en resources.
     * Si no existe o si REGENERATE_GOLDEN=true, genera el archivo en build/generated/golden/
     * para que lo revises y lo copies a src/test/resources.
     */
    fun assertGolden(
        resourcePath: String,
        actual: String,
    ) {
        val normalized = normalize(actual)

        val url = javaClass.getResource(resourcePath)
        if (url != null && System.getenv("REGENERATE_GOLDEN") != "true") {
            val expected = normalize(url.readText())
            assertEquals(expected, normalized, "Golden mismatch for $resourcePath")
            return
        }

        val outFile = File("build/generated/golden$resourcePath")
        outFile.parentFile.mkdirs()
        outFile.toPath().createDirectories()
        outFile.toPath().writeText(normalized, StandardCharsets.UTF_8)
        throw AssertionError(
            "Golden ${if (url == null) "no existía" else "regenerado"}: " +
                "revisá ${outFile.absolutePath} y copiálo a src/test/resources$resourcePath",
        )
    }
}
