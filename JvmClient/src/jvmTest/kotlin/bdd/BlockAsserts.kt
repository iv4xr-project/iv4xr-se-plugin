package bdd

import bdd.repetitiveassert.repeatUntilSuccess
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import spaceEngineers.model.Block
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.blockByCustomName
import spaceEngineers.model.extensions.blocksByCustomName
import testhelp.assertLessThan
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BlockAsserts : AbstractMultiplayerSteps() {


    @Then("Observed grid mass is {double}.")
    fun observed_grid_mass_is(mass: Double) = observers {
        val massByType = definitions.blockDefinitions().associate {
            it.definitionId to it.mass
        }
        assertTrue(
            observer.observeBlocks().grids.any { it.mass == mass.toFloat() },
            "Haven't found any grids with mass $mass, found: ${
                observer.observeBlocks().grids.associate {
                    it.id to "mass: ${it.mass} blocks: ${it.blocks.size}, sum: ${
                        it.blocks.map { massByType[it.definitionId]!! }.sum()
                    }"
                }
            }."
        )
    }

    @When("Block type {string} has mass {double}.")
    fun block_type_has_mass(type: String, mass: Double) = observers {
        val blockDefinition = definitions.blockDefinitions().first {
            it.definitionId.type == type
        }
        assertEquals(mass.toFloat(), blockDefinition.mass)
    }


    @Then("I see no block of type {string}.")
    fun i_see_no_block_of_type(string: String) = observers {
        val observation = observer.observeBlocks()
        assertTrue(
            observation.allBlocks
                .none { it.definitionId.type == string }
        )
    }

    @Then("Target block integrity is less than maximum.")
    fun target_block_integrity_is_less_than_maximum() = observers {
        repeatUntilSuccess {
            checkBlockIntegrityLessThanMaximum(observer.observe().targetBlock)
        }
    }

    @Then("Target block integrity is at maximum.")
    fun target_block_integrity_is_at_maximum() = observers {
        repeatUntilSuccess {
            checkBlockIntegrityAtMaximum(observer.observe().targetBlock)
        }
    }

    @Then("Target block integrity is at {float}%.")
    fun target_block_integrity_is_at(percentageIntegrity: Float) = observers {
        repeatUntilSuccess(delayMs = 1000) {
            val targetBlock = observer.observe().targetBlock
            assertNotNull(targetBlock, "no target block!")
            assertEquals(percentageIntegrity / 100f, targetBlock.integrity / targetBlock.maxIntegrity, 0.0001f)
        }
    }

    @Then("Block {string} integrity is less than maximum.")
    fun block_integrity_is_less_than_maximum(blockName: String) = observers {
        repeatUntilSuccess(delayMs = 1000) {
            val blockObservation = observer.observeBlocks()
            val targetBlock = blockObservation.blockByCustomName(customName = blockName)
            checkBlockIntegrityLessThanMaximum(targetBlock)
        }
    }

    @Then("Block {string} integrity is at maximum.")
    fun block_integrity_is_at_maximum(customName: String) = observers {
        repeatUntilSuccess {
            checkBlockIntegrityAtMaximum(
                observer.observeBlocks().blocksByCustomName(customName).firstOrNull()
            )
        }
    }

    private fun checkBlockIntegrityAtMaximum(block: Block?) {
        assertNotNull(block, "Block not found!")
        assertEquals(block.integrity, block.maxIntegrity)
    }

    private fun checkBlockIntegrityLessThanMaximum(block: Block?) {
        assertNotNull(block, "Block not found!")
        assertLessThan(block.integrity, block.maxIntegrity)
    }
}
