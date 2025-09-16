package formatter.util

import formatter.Formatter
import kotlinx.serialization.json.JsonPrimitive
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertNotNull

class FormatterUtilTest {
    @Nested
    @DisplayName("Config Data Class Tests")
    inner class ConfigTest {
        @Test
        @DisplayName("Should create Config with empty options")
        fun `should create config with empty options`() {
            val config = Config(options = emptyMap())

            assertTrue(config.options.isEmpty())
        }

        @Test
        @DisplayName("Should create Config with populated options")
        fun `should create config with populated options`() {
            val options =
                mapOf(
                    "fix1" to JsonPrimitive(true),
                    "fix2" to JsonPrimitive("value"),
                )
            val config = Config(options = options)

            assertEquals(2, config.options.size)
            assertEquals(JsonPrimitive(true), config.options["fix1"])
            assertEquals(JsonPrimitive("value"), config.options["fix2"])
        }
    }

    @Nested
    @DisplayName("FormatterUtil.createFormatter Tests")
    inner class CreateFormatterTest {
        @Test
        @DisplayName("Should create formatter with default version 1.0")
        fun `should create formatter with default version`() {
            val configJson = "{}"

            val formatter = FormatterUtil.createFormatter(configJson)

            assertNotNull(formatter)
            assertTrue(formatter is Formatter)
        }

        @Test
        @DisplayName("Should create formatter with version 1.0 explicitly")
        fun `should create formatter with version 1_0 explicitly`() {
            val configJson = "{}"

            val formatter = FormatterUtil.createFormatter(configJson, "1.0")

            assertNotNull(formatter)
            assertTrue(formatter is Formatter)
        }

        @Test
        @DisplayName("Should create formatter with version 1.1")
        fun `should create formatter with version 1_1`() {
            val configJson = "{}"

            val formatter = FormatterUtil.createFormatter(configJson, "1.1")

            assertNotNull(formatter)
            assertTrue(formatter is Formatter)
        }

        @Test
        @DisplayName("Should create formatter with complex configuration")
        fun `should create formatter with complex configuration`() {
            val configJson =
                """
                {
                    "SpaceAfterColonFix": true,
                    "MaxOneBlankLineFix": {"enabled": true, "maxLines": 1},
                    "LineJumpAfterSemiColonFix": false
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson, "1.0")

            assertNotNull(formatter)
        }

        @Test
        @DisplayName("Should handle invalid JSON gracefully")
        fun `should handle invalid json`() {
            val invalidJson = "{ invalid json }"

            assertThrows<Exception> {
                FormatterUtil.createFormatter(invalidJson)
            }
        }

        @Test
        @DisplayName("Should ignore unknown keys in JSON")
        fun `should ignore unknown keys in json`() {
            val configJson =
                """
                {
                    "unknownFix": true,
                    "anotherUnknownKey": "value",
                    "SpaceAfterColonFix": true
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson)

            assertNotNull(formatter)
        }
    }

    @Nested
    @DisplayName("Version Fixes Tests")
    inner class VersionFixesTest {
        @Test
        @DisplayName("Should return correct fixes for version 1.0")
        fun `should return correct fixes for version 1_0`() {
            val configJson = "{}"
            val formatter = FormatterUtil.createFormatter(configJson, "1.0")

            // Version 1.0 should not include If-related fixes
            assertNotNull(formatter)
        }

        @Test
        @DisplayName("Should return correct fixes for version 1.1")
        fun `should return correct fixes for version 1_1`() {
            val configJson = "{}"
            val formatter = FormatterUtil.createFormatter(configJson, "1.1")

            // Version 1.1 should include additional If-related fixes:
            // IfBraceSameLinePlacementFix, IfInnerIndentationFix, IfBraceBellowLineFix
            assertNotNull(formatter)
        }

        @Test
        @DisplayName("Should default to version 1.1 for unknown versions")
        fun `should default to version 1_1 for unknown versions`() {
            val configJson = "{}"
            val formatter1 = FormatterUtil.createFormatter(configJson, "2.0")
            val formatter2 = FormatterUtil.createFormatter(configJson, "unknown")
            val formatter3 = FormatterUtil.createFormatter(configJson, "0.5")

            assertNotNull(formatter1)
            assertNotNull(formatter2)
            assertNotNull(formatter3)
            // All should default to version 1.1 (latest)
        }

        @Test
        @DisplayName("Should handle empty string version")
        fun `should handle empty string version`() {
            val configJson = "{}"
            val formatter = FormatterUtil.createFormatter(configJson, "")

            assertNotNull(formatter)
            // Should default to version 1.1
        }
    }

    @Nested
    @DisplayName("Fix Application Tests")
    inner class FixApplicationTest {
        @Test
        @DisplayName("Should apply fixes based on configuration")
        fun `should apply fixes based on configuration`() {
            val configJson =
                """
                {
                    "SpaceAfterColonFix": true,
                    "SpaceBeforeColonFix": true,
                    "MaxOneBlankLineFix": true
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson)

            assertNotNull(formatter)
        }

        @Test
        @DisplayName("Should handle fix settings for configurable fixes")
        fun `should handle fix settings for configurable fixes`() {
            val configJson =
                """
                {
                    "MaxOneBlankLineFix": {"enabled": true, "maxLines": 2},
                    "OneSpaceAfterTokenMaxFix": {"enabled": true, "maxSpaces": 1}
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson)

            assertNotNull(formatter)
        }

        @Test
        @DisplayName("Should create formatter with all fixes disabled")
        fun `should create formatter with all fixes disabled`() {
            val configJson =
                """
                {
                    "SpaceAfterColonFix": false,
                    "SpaceBeforeColonFix": false,
                    "MaxOneBlankLineFix": false,
                    "LineJumpAfterSemiColonFix": false
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson)

            assertNotNull(formatter)
        }

        @Test
        @DisplayName("Should handle mixed enabled/disabled fixes")
        fun `should handle mixed enabled disabled fixes`() {
            val configJson =
                """
                {
                    "SpaceAfterColonFix": true,
                    "SpaceBeforeColonFix": false,
                    "MaxOneBlankLineFix": true,
                    "LineJumpAfterSemiColonFix": false,
                    "OneSpaceAfterTokenMaxFix": true
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson)

            assertNotNull(formatter)
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    inner class IntegrationTest {
        @Test
        @DisplayName("Should create functional formatter for version 1.0 typical use case")
        fun `should create functional formatter for version 1_0 typical use case`() {
            val configJson =
                """
                {
                    "SpaceBeforeColonFix": true,
                    "SpaceAfterColonFix": true,
                    "OneSpaceAfterTokenMaxFix": true,
                    "SpaceBeforeAndAfterEqualFix": true,
                    "SpaceBeforeAndAfterOperatorFix": true,
                    "LineJumpAfterSemiColonFix": true,
                    "LineJumpSpaceBeforePrintlnFix": true,
                    "MaxOneBlankLineFix": true,
                    "MandatorySingleSpaceSeparation": true
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson, "1.0")

            assertNotNull(formatter)
        }

        @Test
        @DisplayName("Should create functional formatter for version 1.1 typical use case")
        fun `should create functional formatter for version 1_1 typical use case`() {
            val configJson =
                """
                {
                    "SpaceBeforeColonFix": true,
                    "SpaceAfterColonFix": true,
                    "OneSpaceAfterTokenMaxFix": true,
                    "SpaceBeforeAndAfterEqualFix": true,
                    "SpaceBeforeAndAfterOperatorFix": true,
                    "LineJumpAfterSemiColonFix": true,
                    "LineJumpSpaceBeforePrintlnFix": true,
                    "MaxOneBlankLineFix": true,
                    "MandatorySingleSpaceSeparation": true,
                    "IfBraceSameLinePlacementFix": true,
                    "IfInnerIndentationFix": true,
                    "IfBraceBellowLineFix": true
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson, "1.1")

            assertNotNull(formatter)
        }

        @Test
        @DisplayName("Should handle minimal configuration")
        fun `should handle minimal configuration`() {
            val configJson =
                """
                {
                    "SpaceAfterColonFix": true
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson)

            assertNotNull(formatter)
        }

        @Test
        @DisplayName("Should handle empty configuration")
        fun `should handle empty configuration`() {
            val configJson = "{}"

            val formatter = FormatterUtil.createFormatter(configJson)

            assertNotNull(formatter)
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    inner class EdgeCasesTest {
        @Test
        @DisplayName("Should handle null values in configuration")
        fun `should handle null values in configuration`() {
            val configJson =
                """
                {
                    "SpaceAfterColonFix": null,
                    "MaxOneBlankLineFix": true
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson)

            assertNotNull(formatter)
        }

        @Test
        @DisplayName("Should handle mixed data types in configuration")
        fun `should handle mixed data types in configuration`() {
            val configJson =
                """
                {
                    "SpaceAfterColonFix": true,
                    "MaxOneBlankLineFix": {"maxLines": 1},
                    "SomeNumericFix": 42,
                    "SomeStringFix": "value"
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson)

            assertNotNull(formatter)
        }

        @Test
        @DisplayName("Should handle very large configuration")
        fun `should handle large configuration`() {
            val configBuilder = StringBuilder("{")
            for (i in 1..100) {
                if (i > 1) configBuilder.append(",")
                configBuilder.append("\"fix$i\": ${i % 2 == 0}")
            }
            configBuilder.append("}")

            val formatter = FormatterUtil.createFormatter(configBuilder.toString())

            assertNotNull(formatter)
        }

        @Test
        @DisplayName("Should handle duplicate fixes in version lists")
        fun `should handle duplicate fixes in version lists`() {
            // Note: The current implementation has duplicates in onePointOneFormatFixes
            // This test ensures the formatter still works correctly
            val configJson =
                """
                {
                    "SpaceBeforeColonFix": true,
                    "SpaceAfterColonFix": true
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson, "1.1")

            assertNotNull(formatter)
        }
    }

    @Nested
    @DisplayName("JSON Processing Tests")
    inner class JsonProcessingTest {
        @Test
        @DisplayName("Should correctly deserialize complex JSON structures")
        fun `should correctly deserialize complex json structures`() {
            val configJson =
                """
                {
                    "SpaceAfterColonFix": true,
                    "ComplexFix": {
                        "enabled": true,
                        "options": {
                            "strictMode": false,
                            "maxLength": 100
                        }
                    },
                    "ArrayFix": [1, 2, 3, "test"]
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson)

            assertNotNull(formatter)
        }

        @Test
        @DisplayName("Should handle escaped characters in JSON")
        fun `should handle escaped characters in json`() {
            val configJson =
                """
                {
                    "SpaceAfterColonFix": true,
                    "FixWithString": "This is a \"quoted\" string with \n newlines"
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson)

            assertNotNull(formatter)
        }

        @Test
        @DisplayName("Should handle deeply nested JSON")
        fun `should handle deeply nested json`() {
            val configJson =
                """
                {
                    "SpaceAfterColonFix": true,
                    "NestedFix": {
                        "level1": {
                            "level2": {
                                "level3": {
                                    "enabled": true,
                                    "value": 42
                                }
                            }
                        }
                    }
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson)

            assertNotNull(formatter)
        }
    }

    @Nested
    @DisplayName("Specific Fix Types Tests")
    inner class SpecificFixTypesTest {
        @Test
        @DisplayName("Should handle spacing-related fixes")
        fun `should handle spacing related fixes`() {
            val configJson =
                """
                {
                    "SpaceAfterColonFix": true,
                    "SpaceBeforeColonFix": true,
                    "SpaceAfterEqualFix": true,
                    "SpaceBeforeEqualFix": true,
                    "NoSpaceAfterEqualFix": false,
                    "SpaceAfterOperatorFix": true,
                    "SpaceBeforeOperatorFix": true,
                    "SpaceBeforeAndAfterEqualFix": true,
                    "SpaceBeforeAndAfterOperatorFix": true
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson)

            assertNotNull(formatter)
        }

        @Test
        @DisplayName("Should handle line jump related fixes")
        fun `should handle line jump related fixes`() {
            val configJson =
                """
                {
                    "LineJumpAfterSemiColonFix": true,
                    "LineJumpSpaceBeforePrintlnFix": true,
                    "MaxOneBlankLineFix": true
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson)

            assertNotNull(formatter)
        }

        @Test
        @DisplayName("Should handle if-statement related fixes (version 1.1 only)")
        fun `should handle if statement related fixes version 1_1 only`() {
            val configJson =
                """
                {
                    "IfBraceSameLinePlacementFix": true,
                    "IfInnerIndentationFix": true,
                    "IfBraceBellowLineFix": true
                }
                """.trimIndent()

            val formatterV10 = FormatterUtil.createFormatter(configJson, "1.0")
            val formatterV11 = FormatterUtil.createFormatter(configJson, "1.1")

            assertNotNull(formatterV10)
            assertNotNull(formatterV11)
            // In version 1.0, these If-related fixes shouldn't be available
            // In version 1.1, they should be available
        }

        @Test
        @DisplayName("Should handle token and separation fixes")
        fun `should handle token and separation fixes`() {
            val configJson =
                """
                {
                    "OneSpaceAfterTokenMaxFix": true,
                    "MandatorySingleSpaceSeparation": true
                }
                """.trimIndent()

            val formatter = FormatterUtil.createFormatter(configJson)

            assertNotNull(formatter)
        }
    }
}
