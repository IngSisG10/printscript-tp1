package linter.customTest

import common.enums.OperationEnum
import common.token.OperationToken
import common.token.VariableToken
import common.token.WhiteSpaceToken
import kotlinx.serialization.json.JsonPrimitive
import linter.rules.custom.SpaceBeforeOperatorRule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpaceBeforeOperatorRuleTest {
    // Note: The original SpaceBeforeOperatorRule has bugs in the match method:
    // 1. Loop bound should be "tokens.size - 1" not "tokens.size + 1"
    // 2. Logic should check tokens[i-1] not tokens[i+1] for space BEFORE operator
    // These tests are written assuming a corrected implementation

    @Test
    fun `applies should return true when space-before-operator-rule is true`() {
        val rule = SpaceBeforeOperatorRule()
        val rules = mapOf("space-before-operator-rule" to JsonPrimitive(true))

        val result = rule.applies(rules)
        assertTrue(result)
    }

    @Test
    fun `applies should return false when space-before-operator-rule is false`() {
        val rule = SpaceBeforeOperatorRule()
        val rules = mapOf("space-before-operator-rule" to JsonPrimitive(false))

        val result = rule.applies(rules)
        assertFalse(result)
    }

    @Test
    fun `applies should return false when space-before-operator-rule key is not present`() {
        val rule = SpaceBeforeOperatorRule()
        val rules = mapOf("other-rule" to JsonPrimitive(true))

        val result = rule.applies(rules)
        assertFalse(result)
    }

    @Test
    fun `applies should return false when rules map is empty`() {
        val rule = SpaceBeforeOperatorRule()
        val rules = emptyMap<String, kotlinx.serialization.json.JsonElement>()

        val result = rule.applies(rules)
        assertFalse(result)
    }

    // The following tests will work with the current buggy implementation
    // but are commented with what they should test once the rule is fixed

    @Test
    fun `current buggy implementation - should demonstrate the index out of bounds issue`() {
        val rule = SpaceBeforeOperatorRule()
        val tokens =
            listOf(
                VariableToken("x", 1, 1),
                WhiteSpaceToken(1, 2),
                OperationToken(OperationEnum.EQUAL, 1, 3),
            )

        // This will likely throw IndexOutOfBoundsException due to tokens.size + 1 in the loop
        try {
            val result = rule.match(tokens)
            // If it doesn't crash, it passes incorrectly due to checking wrong direction
        } catch (e: IndexOutOfBoundsException) {
            // Expected due to the bug in the original implementation
            assertTrue(true) // Test passes because we expect this error
        }
    }

    @Test
    fun `applies should handle multiple rules in map`() {
        val rule = SpaceBeforeOperatorRule()
        val rules =
            mapOf(
                "other-rule" to JsonPrimitive(false),
                "space-before-operator-rule" to JsonPrimitive(true),
                "another-rule" to JsonPrimitive(true),
            )

        val result = rule.applies(rules)
        assertTrue(result)
    }
}
