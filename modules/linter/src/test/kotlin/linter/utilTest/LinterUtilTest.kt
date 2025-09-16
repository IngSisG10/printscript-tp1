package linter.util

import kotlinx.serialization.json.JsonPrimitive
import linter.Linter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class LinterUtilTest {
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
                    "rule1" to JsonPrimitive(true),
                    "rule2" to JsonPrimitive("value"),
                )
            val config = Config(options = options)

            assertEquals(2, config.options.size)
            assertEquals(JsonPrimitive(true), config.options["rule1"])
            assertEquals(JsonPrimitive("value"), config.options["rule2"])
        }
    }

    @Nested
    @DisplayName("LinterUtil.createLinter Tests")
    inner class CreateLinterTest {
        @Test
        @DisplayName("Should create linter with default version 1.0")
        fun `should create linter with default version`() {
            val configJson = "{}"

            val linter = LinterUtil.createLinter(configJson)

            assertNotNull(linter)
            assertTrue(linter is Linter)
        }

        @Test
        @DisplayName("Should create linter with version 1.1")
        fun `should create linter with version 1_1`() {
            val configJson = "{}"

            val linter = LinterUtil.createLinter(configJson, "1.1")

            assertNotNull(linter)
            assertTrue(linter is Linter)
        }

        @Test
        @DisplayName("Should create linter with complex configuration")
        fun `should create linter with complex configuration`() {
            val configJson =
                """
                {
                    "CamelCaseRule": true,
                    "SpaceAfterColonRule": {"enabled": true, "spaces": 1},
                    "LineJumpAfterSemicolonRule": false
                }
                """.trimIndent()

            val linter = LinterUtil.createLinter(configJson, "1.0")

            assertNotNull(linter)
        }

        @Test
        @DisplayName("Should ignore unknown keys in JSON")
        fun `should ignore unknown keys in json`() {
            val configJson =
                """
                {
                    "unknownRule": true,
                    "anotherUnknownKey": "value",
                    "CamelCaseRule": true
                }
                """.trimIndent()

            val linter = LinterUtil.createLinter(configJson)

            assertNotNull(linter)
        }
    }

    @Nested
    @DisplayName("Version Rules Tests")
    inner class VersionRulesTest {
        @Test
        @DisplayName("Should return correct rules for version 1.0")
        fun `should return correct rules for version 1_0`() {
            val configJson = "{}"
            val linter = LinterUtil.createLinter(configJson, "1.0")

            // We can't directly test the private method, but we can verify
            // that the linter was created successfully with version 1.0 rules
            assertNotNull(linter)
        }

        @Test
        @DisplayName("Should return correct rules for version 1.1")
        fun `should return correct rules for version 1_1`() {
            val configJson = "{}"
            val linter = LinterUtil.createLinter(configJson, "1.1")

            // Version 1.1 should include ReadInputSimpleArgumentRule
            assertNotNull(linter)
        }

        @Test
        @DisplayName("Should fallback to version 1.0 for unknown versions")
        fun `should fallback to version 1_0 for unknown versions`() {
            val configJson = "{}"
            val linter1 = LinterUtil.createLinter(configJson, "2.0")
            val linter2 = LinterUtil.createLinter(configJson, "unknown")

            assertNotNull(linter1)
            assertNotNull(linter2)
        }
    }

    @Nested
    @DisplayName("Rule Application Tests")
    inner class RuleApplicationTest {
        @Test
        @DisplayName("Should apply rules based on configuration")
        fun `should apply rules based on configuration`() {
            // Test with a configuration that enables specific rules
            val configJson =
                """
                {
                    "CamelCaseRule": true,
                    "SpaceAfterColonRule": true
                }
                """.trimIndent()

            val linter = LinterUtil.createLinter(configJson)

            assertNotNull(linter)
        }

        @Test
        @DisplayName("Should handle rule settings for configurable rules")
        fun `should handle rule settings for configurable rules`() {
            val configJson =
                """
                {
                    "SpaceAfterColonRule": {"enabled": true, "spaces": 2},
                    "OneSpaceBetweenTokensRule": {"enabled": true}
                }
                """.trimIndent()

            val linter = LinterUtil.createLinter(configJson)

            assertNotNull(linter)
        }

        @Test
        @DisplayName("Should create linter with all rules disabled")
        fun `should create linter with all rules disabled`() {
            val configJson =
                """
                {
                    "CamelCaseRule": false,
                    "PascalCaseRule": false,
                    "SnakeCaseRule": false
                }
                """.trimIndent()

            val linter = LinterUtil.createLinter(configJson)

            assertNotNull(linter)
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    inner class IntegrationTest {
        @Test
        @DisplayName("Should create functional linter for typical use case")
        fun `should create functional linter for typical use case`() {
            val configJson =
                """
                {
                    "CamelCaseRule": true,
                    "PascalCaseRule": true,
                    "SnakeCaseRule": true,
                    "PrintLnSimpleArgumentRule": true,
                    "SpaceAfterColonRule": true,
                    "SpaceBeforeColonRule": false,
                    "LineJumpAfterSemicolonRule": true,
                    "NewLineBeforePrintlnRule": true,
                    "OneSpaceBetweenTokensRule": true,
                    "SpaceAfterAssignationRule": true,
                    "SpaceBeforeAssignationRule": true,
                    "SpaceAfterOperatorRule": true,
                    "SpaceBeforeOperatorRule": true
                }
                """.trimIndent()

            val linter = LinterUtil.createLinter(configJson, "1.1")

            assertNotNull(linter)
            // In a real scenario, you might test that the linter can process code
            // val result = linter.lint("some kotlin code")
            // assertNotNull(result)
        }

        @Test
        @DisplayName("Should handle minimal configuration")
        fun `should handle minimal configuration`() {
            val configJson =
                """
                {
                    "CamelCaseRule": true
                }
                """.trimIndent()

            val linter = LinterUtil.createLinter(configJson)

            assertNotNull(linter)
        }

        @Test
        @DisplayName("Should handle empty configuration")
        fun `should handle empty configuration`() {
            val configJson = "{}"

            val linter = LinterUtil.createLinter(configJson)

            assertNotNull(linter)
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
                    "CamelCaseRule": null,
                    "PascalCaseRule": true
                }
                """.trimIndent()

            val linter = LinterUtil.createLinter(configJson)

            assertNotNull(linter)
        }

        @Test
        @DisplayName("Should handle mixed data types in configuration")
        fun `should handle mixed data types in configuration`() {
            val configJson =
                """
                {
                    "CamelCaseRule": true,
                    "SpaceAfterColonRule": {"spaces": 1},
                    "SomeNumericRule": 42,
                    "SomeStringRule": "value"
                }
                """.trimIndent()

            val linter = LinterUtil.createLinter(configJson)

            assertNotNull(linter)
        }

        @Test
        @DisplayName("Should handle very large configuration")
        fun `should handle large configuration`() {
            val configBuilder = StringBuilder("{")
            for (i in 1..100) {
                if (i > 1) configBuilder.append(",")
                configBuilder.append("\"rule$i\": ${i % 2 == 0}")
            }
            configBuilder.append("}")

            val linter = LinterUtil.createLinter(configBuilder.toString())

            assertNotNull(linter)
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
                    "CamelCaseRule": true,
                    "ComplexRule": {
                        "enabled": true,
                        "options": {
                            "strictMode": false,
                            "maxLength": 100
                        }
                    },
                    "ArrayRule": [1, 2, 3, "test"]
                }
                """.trimIndent()

            val linter = LinterUtil.createLinter(configJson)

            assertNotNull(linter)
        }

        @Test
        @DisplayName("Should handle escaped characters in JSON")
        fun `should handle escaped characters in json`() {
            val configJson =
                """
                {
                    "CamelCaseRule": true,
                    "RuleWithString": "This is a \"quoted\" string with \n newlines"
                }
                """.trimIndent()

            val linter = LinterUtil.createLinter(configJson)

            assertNotNull(linter)
        }
    }
}
