package bdd

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import spaceEngineers.model.extensions.allBlocks
import kotlin.test.assertEquals
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
}
